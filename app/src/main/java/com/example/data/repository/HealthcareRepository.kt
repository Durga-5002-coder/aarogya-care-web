package com.example.data.repository

import android.content.Context
import com.example.data.auth.AadhaarFirebaseAuthManager
import com.example.data.local.AppDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.DoctorSummaryRecord
import com.example.data.model.HealthMetricRecord
import com.example.data.model.MedicalReport
import com.example.data.model.MedicationReminder
import com.example.data.model.MessageSender
import com.example.data.model.MetricType
import com.example.data.model.OpRegistration
import com.example.data.model.ReportCategory
import com.example.data.model.ReportHealthStatus
import com.example.data.model.UserProfile
import com.example.data.remote.AiDoctorSummaryResult
import com.example.data.remote.AiReportAnalysisResult
import com.example.data.remote.AiTriageResult
import com.example.data.remote.GeminiMedicalAssistant
import com.example.notification.MedicationNotificationHelper
import com.example.notification.MedicationScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Random

class HealthcareRepository(private val context: Context) {
    private val database = AppDatabase.getDatabase(context)
    private val userDao = database.userDao()
    private val reportDao = database.medicalReportDao()
    private val opDao = database.opRegistrationDao()
    private val chatDao = database.chatMessageDao()
    private val summaryDao = database.doctorSummaryDao()
    private val medicationDao = database.medicationReminderDao()
    private val metricDao = database.healthMetricDao()

    private val _currentUserAadhaar = MutableStateFlow<String?>(null)
    val currentUserAadhaar: StateFlow<String?> = _currentUserAadhaar.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            seedDefaultDataIfEmpty()
        }
    }

    private suspend fun seedDefaultDataIfEmpty() {
        val existing = userDao.getUserByAadhaarSync("5482 9104 3821")
        if (existing == null) {
            val demoUser = UserProfile(
                aadhaarNumber = "5482 9104 3821",
                fullName = "Durga Reddy Nidrabingi",
                age = 28,
                dateOfBirth = "14/08/1998",
                gender = "Male",
                phoneNumber = "+91 98765 43210",
                address = "Plot No. 42, Silicon Valley Colony, Madhapur, Hyderabad, Telangana - 500081",
                abhaId = "91-5482-9104-3821",
                bloodGroup = "O+",
                isAadhaarVerified = true,
                isPhoneVerified = true
            )
            userDao.insertUser(demoUser)
            _currentUserAadhaar.value = demoUser.aadhaarNumber

            // Seed Initial Medical Reports
            val report1 = MedicalReport(
                userAadhaar = demoUser.aadhaarNumber,
                title = "Complete Blood Count (CBC) Routine",
                category = ReportCategory.LAB_TEST.name,
                hospitalOrLabName = "AIIMS New Delhi Central Pathology Lab",
                doctorOrTechnician = "Dr. R. K. Mukherjee, MD",
                dateString = "20 Aug 2026",
                summary = "Hemoglobin slightly low at 11.4 g/dL. Total leukocyte count and platelets are within optimal physiological limits.",
                keyMetrics = "• Hb: 11.4 g/dL (Ref: 13.0 - 17.0)\n• WBC: 6,800 /µL (Ref: 4,000 - 11,000)\n• Platelets: 2.3 Lakhs/µL\n• Neutrophils: 62%",
                doctorAdvice = "Increase iron-rich leafy greens, jaggery, and citrus fruits. Repeat CBC test in 6 weeks.",
                status = ReportHealthStatus.ATTENTION.name,
                timestamp = System.currentTimeMillis() - 86400000L * 3
            )

            val report2 = MedicalReport(
                userAadhaar = demoUser.aadhaarNumber,
                title = "Chest X-Ray (PA View)",
                category = ReportCategory.RADIOLOGY.name,
                hospitalOrLabName = "Apollo Diagnostics Imaging Center",
                doctorOrTechnician = "Dr. Vivek Chawla, DMRD",
                dateString = "15 Aug 2026",
                summary = "Bilateral lung fields clear of consolidation or infiltration. Costophrenic angles normal. Cardiac silhouette normal.",
                keyMetrics = "• Lung Fields: Clear\n• CP Angles: Sharp\n• CTR: 0.44\n• Bony cage: Intact",
                doctorAdvice = "Chest imaging shows clear lungs. No acute radiographic pathology.",
                status = ReportHealthStatus.NORMAL.name,
                timestamp = System.currentTimeMillis() - 86400000L * 8
            )

            val report3 = MedicalReport(
                userAadhaar = demoUser.aadhaarNumber,
                title = "OP Prescription - Seasonal Flu",
                category = ReportCategory.PRESCRIPTION.name,
                hospitalOrLabName = "Max Super Speciality Hospital OPD",
                doctorOrTechnician = "Dr. S. K. Verma, MD (Medicine)",
                dateString = "10 Aug 2026",
                summary = "Consultation for viral rhinitis and intermittent dry cough. Mild pharyngeal congestion.",
                keyMetrics = "• BP: 118/76 mmHg\n• Pulse: 72 bpm\n• SpO2: 98%\n• Temp: 98.6°F",
                doctorAdvice = "Tab. Paracetamol 650mg SOS, Tab. Montelukast-Levocetirizine 1 tab at bedtime for 5 days. Warm saline gargles.",
                status = ReportHealthStatus.NORMAL.name,
                timestamp = System.currentTimeMillis() - 86400000L * 13
            )

            reportDao.insertReport(report1)
            reportDao.insertReport(report2)
            reportDao.insertReport(report3)

            // Seed Initial OP Registration Ticket
            val op1 = OpRegistration(
                tokenNumber = "AIIMS-MED-2026-104",
                userAadhaar = demoUser.aadhaarNumber,
                patientName = demoUser.fullName,
                patientAge = demoUser.age,
                patientPhone = demoUser.phoneNumber,
                hospitalName = "AIIMS New Delhi (Main Campus)",
                department = "General Medicine",
                doctorName = "Dr. Rajeshwar Rao, Senior Consultant",
                appointmentDate = "Tomorrow, 09:30 AM",
                timeSlot = "Slot 1 (09:00 AM - 10:30 AM)",
                symptoms = "Mild fatigue, seasonal allergic cough, routine wellness checkup",
                roomNumber = "OP Block B, Room 108",
                status = "CONFIRMED",
                bookingTimestamp = System.currentTimeMillis() - 3600000L
            )
            opDao.insertRegistration(op1)

            // Seed Welcome Chat
            val welcomeMsg = ChatMessage(
                userAadhaar = demoUser.aadhaarNumber,
                sender = MessageSender.AI_BOT.name,
                text = "Namaste Durga Reddy! I am AarogyaCare AI, your personal clinical triage assistant. How are you feeling today? You can describe any symptoms or ask for emergency first-aid instructions anytime.",
                firstAidSteps = null,
                recommendedSpecialist = "General Medicine",
                urgencyLevel = "Low"
            )
            chatDao.insertMessage(welcomeMsg)

            // Seed Medication Reminders
            seedMedicationsForUser(demoUser.aadhaarNumber)

            // Seed Historical Health Metrics
            seedHealthMetricsForUser(demoUser.aadhaarNumber)
        } else {
            _currentUserAadhaar.value = existing.aadhaarNumber
            val existingMeds = medicationDao.getRemindersForUserSync(existing.aadhaarNumber)
            if (existingMeds.isEmpty()) {
                seedMedicationsForUser(existing.aadhaarNumber)
            }
            val existingMetrics = metricDao.getAllMetricsForUserSync(existing.aadhaarNumber)
            if (existingMetrics.isEmpty()) {
                seedHealthMetricsForUser(existing.aadhaarNumber)
            }
        }
    }

    private suspend fun seedHealthMetricsForUser(aadhaar: String) {
        val now = System.currentTimeMillis()
        val dayMs = 86400000L
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val list = mutableListOf<HealthMetricRecord>()

        // 1. Blood Pressure Readings (Systolic / Diastolic) across 30 days
        val bpReadings = listOf(
            Triple(30, 138.0, 88.0),
            Triple(25, 135.0, 86.0),
            Triple(20, 132.0, 84.0),
            Triple(16, 128.0, 82.0),
            Triple(12, 126.0, 82.0),
            Triple(8, 124.0, 80.0),
            Triple(5, 122.0, 78.0),
            Triple(2, 120.0, 78.0),
            Triple(0, 118.0, 76.0)
        )
        bpReadings.forEach { (daysAgo, sys, dia) ->
            val time = now - (daysAgo * dayMs)
            list.add(
                HealthMetricRecord(
                    userAadhaar = aadhaar,
                    metricType = MetricType.BLOOD_PRESSURE.name,
                    valuePrimary = sys,
                    valueSecondary = dia,
                    unit = "mmHg",
                    contextTag = "Resting Morning",
                    notes = if (sys >= 135) "Mild hypertension recorded during triage" else "Under controlled therapy with Telmisartan",
                    dateString = sdf.format(Date(time)),
                    timestamp = time
                )
            )
        }

        // 2. Blood Glucose (Fasting & Post-Meal)
        val glucoseReadings = listOf(
            Triple(30, 118.0, "Fasting"),
            Triple(30, 168.0, "Post-Meal"),
            Triple(24, 112.0, "Fasting"),
            Triple(24, 154.0, "Post-Meal"),
            Triple(18, 106.0, "Fasting"),
            Triple(18, 146.0, "Post-Meal"),
            Triple(12, 100.0, "Fasting"),
            Triple(12, 138.0, "Post-Meal"),
            Triple(6, 96.0, "Fasting"),
            Triple(6, 132.0, "Post-Meal"),
            Triple(1, 92.0, "Fasting"),
            Triple(1, 128.0, "Post-Meal")
        )
        glucoseReadings.forEach { (daysAgo, value, tag) ->
            val time = now - (daysAgo * dayMs)
            list.add(
                HealthMetricRecord(
                    userAadhaar = aadhaar,
                    metricType = MetricType.BLOOD_GLUCOSE.name,
                    valuePrimary = value,
                    unit = "mg/dL",
                    contextTag = tag,
                    notes = if (tag == "Fasting") "Early morning fasting test" else "2 hours post lunch test",
                    dateString = sdf.format(Date(time)),
                    timestamp = time
                )
            )
        }

        // 3. Heart Rate (Pulse)
        val hrReadings = listOf(
            Pair(28, 84.0),
            Pair(21, 80.0),
            Pair(14, 76.0),
            Pair(7, 72.0),
            Pair(2, 70.0),
            Pair(0, 68.0)
        )
        hrReadings.forEach { (daysAgo, bpm) ->
            val time = now - (daysAgo * dayMs)
            list.add(
                HealthMetricRecord(
                    userAadhaar = aadhaar,
                    metricType = MetricType.HEART_RATE.name,
                    valuePrimary = bpm,
                    unit = "bpm",
                    contextTag = "Resting Pulse",
                    notes = "Normal Sinus Rhythm",
                    dateString = sdf.format(Date(time)),
                    timestamp = time
                )
            )
        }

        // 4. HbA1c (Quarterly Glycated Hemoglobin %)
        val hba1cReadings = listOf(
            Pair(270, 6.8), // 9 months ago
            Pair(180, 6.4), // 6 months ago
            Pair(90, 5.9),  // 3 months ago
            Pair(5, 5.5)    // Recent
        )
        hba1cReadings.forEach { (daysAgo, hba1c) ->
            val time = now - (daysAgo * dayMs)
            list.add(
                HealthMetricRecord(
                    userAadhaar = aadhaar,
                    metricType = MetricType.HBA1C.name,
                    valuePrimary = hba1c,
                    unit = "%",
                    contextTag = "Clinical Lab",
                    notes = "Significant positive HbA1c reduction towards normal range (<5.7%)",
                    dateString = sdf.format(Date(time)),
                    timestamp = time
                )
            )
        }

        // 5. Total Cholesterol (mg/dL)
        val cholReadings = listOf(
            Pair(180, 224.0),
            Pair(90, 195.0),
            Pair(10, 178.0)
        )
        cholReadings.forEach { (daysAgo, chol) ->
            val time = now - (daysAgo * dayMs)
            list.add(
                HealthMetricRecord(
                    userAadhaar = aadhaar,
                    metricType = MetricType.CHOLESTEROL.name,
                    valuePrimary = chol,
                    unit = "mg/dL",
                    contextTag = "Lipid Panel",
                    notes = "Improved lipid profile with dietary changes",
                    dateString = sdf.format(Date(time)),
                    timestamp = time
                )
            )
        }

        // 6. Oxygen SpO2
        val spo2Readings = listOf(
            Pair(20, 97.0),
            Pair(10, 98.0),
            Pair(1, 99.0)
        )
        spo2Readings.forEach { (daysAgo, spo2) ->
            val time = now - (daysAgo * dayMs)
            list.add(
                HealthMetricRecord(
                    userAadhaar = aadhaar,
                    metricType = MetricType.OXYGEN_SPO2.name,
                    valuePrimary = spo2,
                    unit = "%",
                    contextTag = "Pulse Oximeter",
                    notes = "Room air saturation optimal",
                    dateString = sdf.format(Date(time)),
                    timestamp = time
                )
            )
        }

        // 7. Body Weight
        val weightReadings = listOf(
            Pair(90, 74.5),
            Pair(60, 73.0),
            Pair(30, 71.5),
            Pair(2, 69.8)
        )
        weightReadings.forEach { (daysAgo, wt) ->
            val time = now - (daysAgo * dayMs)
            list.add(
                HealthMetricRecord(
                    userAadhaar = aadhaar,
                    metricType = MetricType.BODY_WEIGHT.name,
                    valuePrimary = wt,
                    unit = "kg",
                    contextTag = "Digital Scale",
                    notes = "Healthy BMI progress: 23.4",
                    dateString = sdf.format(Date(time)),
                    timestamp = time
                )
            )
        }

        metricDao.insertMetrics(list)
    }

    private suspend fun seedMedicationsForUser(aadhaar: String) {
        val med1 = MedicationReminder(
            userAadhaar = aadhaar,
            medicineName = "Pantoprazole 40mg",
            dosage = "1 Capsule",
            instruction = "Before Breakfast (Empty Stomach)",
            timeHour = 7,
            timeMinute = 30,
            category = "Gastro Care",
            isActive = true,
            streakDays = 5
        )
        val med2 = MedicationReminder(
            userAadhaar = aadhaar,
            medicineName = "Metformin 500mg",
            dosage = "1 Tablet",
            instruction = "After Breakfast with water",
            timeHour = 8,
            timeMinute = 30,
            category = "Diabetes",
            isActive = true,
            streakDays = 12
        )
        val med3 = MedicationReminder(
            userAadhaar = aadhaar,
            medicineName = "Telmisartan 40mg",
            dosage = "1 Tablet",
            instruction = "After Lunch",
            timeHour = 13,
            timeMinute = 30,
            category = "Blood Pressure",
            isActive = true,
            streakDays = 8
        )
        val med4 = MedicationReminder(
            userAadhaar = aadhaar,
            medicineName = "Multivitamin & Zinc",
            dosage = "1 Tablet",
            instruction = "At Bedtime after dinner",
            timeHour = 21,
            timeMinute = 0,
            category = "Vitamin/Supplement",
            isActive = true,
            streakDays = 14
        )

        val id1 = medicationDao.insertReminder(med1)
        val id2 = medicationDao.insertReminder(med2)
        val id3 = medicationDao.insertReminder(med3)
        val id4 = medicationDao.insertReminder(med4)

        // Schedule alarms
        MedicationScheduler.scheduleMedicationAlarm(context, med1.copy(id = id1))
        MedicationScheduler.scheduleMedicationAlarm(context, med2.copy(id = id2))
        MedicationScheduler.scheduleMedicationAlarm(context, med3.copy(id = id3))
        MedicationScheduler.scheduleMedicationAlarm(context, med4.copy(id = id4))
    }

    // --- Authentication Operations ---

    fun setCurrentUser(aadhaar: String) {
        _currentUserAadhaar.value = aadhaar
    }

    fun getUser(aadhaar: String): Flow<UserProfile?> = userDao.getUser(aadhaar)

    fun getLatestUser(): Flow<UserProfile?> = userDao.getLatestUser()

    fun getAllUsers(): Flow<List<UserProfile>> = userDao.getAllUsers()

    suspend fun authenticateWithAadhaar(aadhaarNumber: String, enteredOtp: String): Result<UserProfile> = withContext(Dispatchers.IO) {
        val cleanAadhaar = formatAadhaar(aadhaarNumber)
        val existingUser = userDao.getUserByAadhaarSync(cleanAadhaar)
        val patientName = existingUser?.fullName ?: "Aadhaar Cardholder"

        // 1. Authenticate with Firebase Authentication
        val firebaseResult = AadhaarFirebaseAuthManager.authenticateWithAadhaar(
            context = context,
            aadhaarNumber = cleanAadhaar,
            otp = enteredOtp,
            patientName = patientName
        )

        val firebaseUid = firebaseResult.getOrNull()?.uid

        if (existingUser != null) {
            val updatedUser = if (existingUser.firebaseUid != firebaseUid && firebaseUid != null) {
                val updated = existingUser.copy(firebaseUid = firebaseUid)
                userDao.insertUser(updated)
                updated
            } else {
                existingUser
            }
            _currentUserAadhaar.value = updatedUser.aadhaarNumber
            return@withContext Result.success(updatedUser)
        }

        // Auto-provision demo Aadhaar profile if standard 12-digit number provided
        if (cleanAadhaar.filter { it.isDigit() }.length == 12) {
            val newUser = UserProfile(
                aadhaarNumber = cleanAadhaar,
                fullName = patientName,
                age = 32,
                dateOfBirth = "01/01/1994",
                gender = "Male",
                phoneNumber = "+91 98480 22338",
                address = "H.No 12-34, Main Road, New Delhi - 110001",
                abhaId = "91-${cleanAadhaar.replace(" ", "-")}",
                bloodGroup = "B+",
                firebaseUid = firebaseUid
            )
            userDao.insertUser(newUser)
            _currentUserAadhaar.value = newUser.aadhaarNumber
            return@withContext Result.success(newUser)
        }

        return@withContext Result.failure(Exception("Invalid 12-digit Aadhaar Number or OTP verification failed"))
    }

    suspend fun registerNewUser(
        fullName: String,
        age: Int,
        gender: String,
        phone: String,
        aadhaarInput: String,
        address: String
    ): Result<UserProfile> = withContext(Dispatchers.IO) {
        val formattedAadhaar = formatAadhaar(aadhaarInput)
        val abha = "91-${formattedAadhaar.replace(" ", "-")}"

        // Authenticate with Firebase Auth
        val firebaseResult = AadhaarFirebaseAuthManager.authenticateWithAadhaar(
            context = context,
            aadhaarNumber = formattedAadhaar,
            otp = "123456",
            patientName = fullName
        )
        val firebaseUid = firebaseResult.getOrNull()?.uid

        val user = UserProfile(
            aadhaarNumber = formattedAadhaar,
            fullName = fullName,
            age = age,
            dateOfBirth = "15/06/${2026 - age}",
            gender = gender,
            phoneNumber = phone,
            address = address.ifBlank { "Resident of India - UIDAI Verified" },
            abhaId = abha,
            isAadhaarVerified = true,
            isPhoneVerified = true,
            firebaseUid = firebaseUid
        )
        userDao.insertUser(user)
        _currentUserAadhaar.value = user.aadhaarNumber
        return@withContext Result.success(user)
    }

    fun logoutUser() {
        AadhaarFirebaseAuthManager.signOut(context)
        _currentUserAadhaar.value = null
    }

    fun formatAadhaar(raw: String): String {
        val digits = raw.filter { it.isDigit() }.take(12)
        return when {
            digits.length > 8 -> "${digits.substring(0, 4)} ${digits.substring(4, 8)} ${digits.substring(8)}"
            digits.length > 4 -> "${digits.substring(0, 4)} ${digits.substring(4)}"
            else -> digits
        }
    }

    // --- Report Operations ---

    fun getReportsForUser(aadhaar: String): Flow<List<MedicalReport>> = reportDao.getReportsForUser(aadhaar)

    suspend fun saveReport(report: MedicalReport): Long = reportDao.insertReport(report)

    suspend fun deleteReport(id: Long) = reportDao.deleteReportById(id)

    suspend fun analyzeAndSaveScannedReport(
        userAadhaar: String,
        reportText: String,
        fileName: String
    ): MedicalReport = withContext(Dispatchers.IO) {
        val analysis = GeminiMedicalAssistant.analyzeReportContent(reportText, fileName)
        val newReport = MedicalReport(
            userAadhaar = userAadhaar,
            title = analysis.title,
            category = analysis.category.name,
            hospitalOrLabName = analysis.labOrHospital,
            doctorOrTechnician = analysis.doctorOrTech,
            dateString = analysis.dateString,
            summary = analysis.summary,
            keyMetrics = analysis.keyMetrics,
            doctorAdvice = analysis.doctorAdvice,
            status = analysis.status.name,
            rawText = reportText
        )
        val id = reportDao.insertReport(newReport)
        return@withContext newReport.copy(id = id)
    }

    // --- OP Registration Operations ---

    fun getRegistrationsForUser(aadhaar: String): Flow<List<OpRegistration>> = opDao.getRegistrationsForUser(aadhaar)

    suspend fun registerOpConsultation(
        userProfile: UserProfile,
        hospitalName: String,
        department: String,
        doctorName: String,
        appointmentDate: String,
        timeSlot: String,
        symptoms: String,
        clinicalSummaryText: String? = null
    ): OpRegistration = withContext(Dispatchers.IO) {
        val randCode = (100..999).random()
        val hospCode = hospitalName.take(4).uppercase().replace(" ", "")
        val token = "OP-$hospCode-2026-$randCode"
        val roomNo = "Room ${(101..120).random()} (OP Wing)"

        val reg = OpRegistration(
            tokenNumber = token,
            userAadhaar = userProfile.aadhaarNumber,
            patientName = userProfile.fullName,
            patientAge = userProfile.age,
            patientPhone = userProfile.phoneNumber,
            hospitalName = hospitalName,
            department = department,
            doctorName = doctorName,
            appointmentDate = appointmentDate,
            timeSlot = timeSlot,
            symptoms = symptoms,
            clinicalSummaryText = clinicalSummaryText,
            roomNumber = roomNo,
            status = "CONFIRMED"
        )
        val id = opDao.insertRegistration(reg)
        return@withContext reg.copy(id = id)
    }

    // --- Chat & Triage Operations ---

    fun getChatMessages(aadhaar: String): Flow<List<ChatMessage>> = chatDao.getMessagesForUser(aadhaar)

    suspend fun sendChatMessage(
        userAadhaar: String,
        messageText: String,
        userProfile: UserProfile?,
        history: List<ChatMessage>
    ): ChatMessage = withContext(Dispatchers.IO) {
        val userMsg = ChatMessage(
            userAadhaar = userAadhaar,
            sender = MessageSender.USER.name,
            text = messageText
        )
        chatDao.insertMessage(userMsg)

        val aiResult: AiTriageResult = GeminiMedicalAssistant.getTriageResponse(
            userMessage = messageText,
            conversationHistory = history + userMsg,
            userProfile = userProfile
        )

        val botMsg = ChatMessage(
            userAadhaar = userAadhaar,
            sender = MessageSender.AI_BOT.name,
            text = aiResult.replyText,
            firstAidSteps = aiResult.firstAidInstructions,
            recommendedSpecialist = aiResult.recommendedSpecialist,
            urgencyLevel = aiResult.urgencyLevel
        )
        val botId = chatDao.insertMessage(botMsg)
        return@withContext botMsg.copy(id = botId)
    }

    suspend fun generateAndSaveMedicalTriageReport(
        userProfile: UserProfile,
        chatHistory: List<ChatMessage>
    ): MedicalReport = withContext(Dispatchers.IO) {
        val recentUserSymptoms = chatHistory.filter { it.sender == MessageSender.USER.name }.takeLast(3).joinToString("; ") { it.text }
        val recentFirstAid = chatHistory.mapNotNull { it.firstAidSteps }.lastOrNull() ?: "Standard resting and symptom monitoring"
        val specialist = chatHistory.mapNotNull { it.recommendedSpecialist }.lastOrNull() ?: "General Medicine"

        val currentDate = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
        val report = MedicalReport(
            userAadhaar = userProfile.aadhaarNumber,
            title = "AI Triage & Clinical Assessment Slip",
            category = ReportCategory.OP_SUMMARY.name,
            hospitalOrLabName = "AarogyaCare AI Digital Health Node",
            doctorOrTechnician = "Aarogya Clinical AI Bot",
            dateString = currentDate,
            summary = "Patient evaluated for complaints of: $recentUserSymptoms. Automated triage assigned for Outpatient $specialist review.",
            keyMetrics = "• Reported Chief Symptoms: $recentUserSymptoms\n• First-Aid Suggested: $recentFirstAid\n• Triage Category: Priority Outpatient\n• Recommended OP Specialty: $specialist",
            doctorAdvice = "Present this digital triage slip at the hospital OP counter or upload it during online doctor consultation.",
            status = ReportHealthStatus.NORMAL.name
        )
        val id = reportDao.insertReport(report)
        return@withContext report.copy(id = id)
    }

    // --- Doctor Summary Operations ---

    fun getDoctorSummaries(aadhaar: String): Flow<List<DoctorSummaryRecord>> = summaryDao.getSummariesForUser(aadhaar)

    suspend fun createDoctorSummary(
        profile: UserProfile,
        symptomsText: String,
        targetHospital: String,
        targetDepartment: String,
        reports: List<MedicalReport>
    ): DoctorSummaryRecord = withContext(Dispatchers.IO) {
        val aiSummary = GeminiMedicalAssistant.generateClinicalDoctorSummary(
            profile = profile,
            symptomsText = symptomsText,
            targetHospital = targetHospital,
            targetDepartment = targetDepartment,
            previousReports = reports
        )

        val record = DoctorSummaryRecord(
            userAadhaar = profile.aadhaarNumber,
            patientName = profile.fullName,
            patientAge = profile.age,
            patientGender = profile.gender,
            chiefComplaint = aiSummary.chiefComplaint,
            historyOfPresentIllness = aiSummary.hpi,
            pertinentNegativesAndFlags = aiSummary.pertinentFlags,
            suggestedTriageCategory = aiSummary.triageCategory,
            recommendedQuestionsForDoctor = aiSummary.recommendedQuestions,
            targetHospital = targetHospital,
            targetDepartment = targetDepartment,
            isSubmittedToHospital = false
        )
        val id = summaryDao.insertSummary(record)
        return@withContext record.copy(id = id)
    }

    suspend fun submitSummaryToHospitalOpSystem(
        summaryId: Long,
        hospitalName: String,
        department: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val summary = summaryDao.getSummaryById(summaryId) ?: return@withContext Result.failure(Exception("Summary not found"))
        val ackToken = "ACK-HOSP-${(1000..9999).random()}-OP"
        val updated = summary.copy(
            isSubmittedToHospital = true,
            hospitalAckToken = ackToken,
            submissionTimestamp = System.currentTimeMillis()
        )
        summaryDao.updateSummary(updated)
        return@withContext Result.success(ackToken)
    }

    // --- Medication Reminder Operations ---

    fun getMedicationReminders(aadhaar: String): Flow<List<MedicationReminder>> =
        medicationDao.getRemindersForUser(aadhaar)

    suspend fun addMedicationReminder(reminder: MedicationReminder): Long = withContext(Dispatchers.IO) {
        val id = medicationDao.insertReminder(reminder)
        val created = reminder.copy(id = id)
        if (created.isActive) {
            MedicationScheduler.scheduleMedicationAlarm(context, created)
        }
        return@withContext id
    }

    suspend fun updateMedicationReminder(reminder: MedicationReminder) = withContext(Dispatchers.IO) {
        medicationDao.updateReminder(reminder)
        if (reminder.isActive) {
            MedicationScheduler.scheduleMedicationAlarm(context, reminder)
        } else {
            MedicationScheduler.cancelMedicationAlarm(context, reminder.id)
        }
    }

    suspend fun toggleMedicationActive(reminder: MedicationReminder, isActive: Boolean) = withContext(Dispatchers.IO) {
        medicationDao.updateReminderActiveStatus(reminder.id, isActive)
        val updated = reminder.copy(isActive = isActive)
        if (isActive) {
            MedicationScheduler.scheduleMedicationAlarm(context, updated)
        } else {
            MedicationScheduler.cancelMedicationAlarm(context, reminder.id)
        }
    }

    suspend fun markMedicationTaken(reminder: MedicationReminder) = withContext(Dispatchers.IO) {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val newStreak = if (reminder.lastTakenDate != todayStr) reminder.streakDays + 1 else reminder.streakDays
        medicationDao.markReminderTaken(reminder.id, todayStr, newStreak)
    }

    suspend fun deleteMedicationReminder(reminder: MedicationReminder) = withContext(Dispatchers.IO) {
        MedicationScheduler.cancelMedicationAlarm(context, reminder.id)
        medicationDao.deleteReminder(reminder)
    }

    fun triggerTestNotification(reminder: MedicationReminder) {
        MedicationScheduler.triggerInstantTestNotification(context, reminder)
    }

    // --- Health Metrics & Historical Vitals Operations ---

    fun getAllHealthMetrics(aadhaar: String): Flow<List<HealthMetricRecord>> =
        metricDao.getAllMetricsForUser(aadhaar)

    fun getHealthMetricsByType(aadhaar: String, type: String): Flow<List<HealthMetricRecord>> =
        metricDao.getMetricsByType(aadhaar, type)

    fun getLatestMetric(aadhaar: String, type: String): Flow<HealthMetricRecord?> =
        metricDao.getLatestMetric(aadhaar, type)

    suspend fun addHealthMetric(metric: HealthMetricRecord): Long = withContext(Dispatchers.IO) {
        metricDao.insertMetric(metric)
    }

    suspend fun updateHealthMetric(metric: HealthMetricRecord) = withContext(Dispatchers.IO) {
        metricDao.updateMetric(metric)
    }

    suspend fun deleteHealthMetric(metric: HealthMetricRecord) = withContext(Dispatchers.IO) {
        metricDao.deleteMetric(metric)
    }
}

