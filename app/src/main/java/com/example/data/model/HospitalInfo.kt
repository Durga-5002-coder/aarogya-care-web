package com.example.data.model

data class HospitalInfo(
    val id: String,
    val name: String,
    val city: String,
    val state: String,
    val type: String, // "Government Central", "Super Speciality", "Autonomous Medical Institute"
    val opHours: String,
    val opConsultationFee: String,
    val officialOpPortalUrl: String,
    val emergencyPhone: String,
    val departments: List<String>,
    val bannerColor: Long = 0xFF0D9488
)

object HospitalDirectory {
    val hospitals = listOf(
        HospitalInfo(
            id = "aiims_delhi",
            name = "AIIMS New Delhi (Main Campus)",
            city = "New Delhi",
            state = "Delhi NCR",
            type = "Autonomous Apex Medical Institute",
            opHours = "08:00 AM - 01:00 PM (Mon-Sat)",
            opConsultationFee = "₹10 (Subsidized)",
            officialOpPortalUrl = "https://ors.gov.in/index.html",
            emergencyPhone = "011-26593677",
            departments = listOf(
                "General Medicine",
                "Cardiology & CTC",
                "Orthopedics & Trauma",
                "Pediatrics & Child Care",
                "Neurology & Neuro-Surgery",
                "Pulmonology & Chest Medicine",
                "Dermatology & Venereology",
                "Ophthalmology (RP Centre)",
                "ENT & Head-Neck Surgery",
                "Gastroenterology"
            )
        ),
        HospitalInfo(
            id = "apollo_hospitals",
            name = "Apollo Hospitals & Health City",
            city = "Hyderabad & Multi-city",
            state = "Telangana / National",
            type = "Super Speciality Healthcare Network",
            opHours = "08:30 AM - 08:00 PM (Daily)",
            opConsultationFee = "₹800 - ₹1200",
            officialOpPortalUrl = "https://www.apollohospitals.com/book-appointment",
            emergencyPhone = "1066",
            departments = listOf(
                "General Medicine",
                "Cardiovascular Sciences",
                "Orthopedics & Joint Replacement",
                "Pediatric Care",
                "Neurology",
                "Dermatology & Cosmetology",
                "Endocrinology & Diabetology",
                "ENT & Sinus Surgery"
            )
        ),
        HospitalInfo(
            id = "fortis_healthcare",
            name = "Fortis Memorial Research Institute",
            city = "Gurugram / Multi-city",
            state = "Haryana / National",
            type = "Tertiary & Quaternary Care",
            opHours = "09:00 AM - 06:00 PM (Mon-Sat)",
            opConsultationFee = "₹900 - ₹1400",
            officialOpPortalUrl = "https://www.fortishealthcare.com/book-an-appointment",
            emergencyPhone = "105010",
            departments = listOf(
                "General Internal Medicine",
                "Cardiac Sciences",
                "Bone & Joint Institute",
                "Child Health & Neonatology",
                "Neurosciences",
                "Pulmonology & Critical Care",
                "Dermatology"
            )
        ),
        HospitalInfo(
            id = "manipal_hospitals",
            name = "Manipal Hospitals (Old Airport Rd)",
            city = "Bengaluru",
            state = "Karnataka",
            type = "Multi-Speciality Hospital",
            opHours = "08:00 AM - 07:00 PM (Mon-Sat)",
            opConsultationFee = "₹750 - ₹1100",
            officialOpPortalUrl = "https://www.manipalhospitals.com/appointments/",
            emergencyPhone = "080-22221111",
            departments = listOf(
                "General Medicine",
                "Cardiology",
                "Orthopedics",
                "Pediatrics",
                "Dermatology",
                "Neurology",
                "Gastroenterology"
            )
        ),
        HospitalInfo(
            id = "max_healthcare",
            name = "Max Super Speciality Hospital",
            city = "Saket, New Delhi",
            state = "Delhi NCR",
            type = "Super Speciality Hospital",
            opHours = "08:30 AM - 07:30 PM (Mon-Sat)",
            opConsultationFee = "₹900 - ₹1300",
            officialOpPortalUrl = "https://www.maxhealthcare.in/book-an-appointment",
            emergencyPhone = "011-40554055",
            departments = listOf(
                "Internal Medicine",
                "Cardiology",
                "Orthopedics",
                "Pediatrics",
                "Dermatology",
                "Chest & Respiratory Medicine",
                "Eye Care"
            )
        ),
        HospitalInfo(
            id = "kgh_visakhapatnam",
            name = "King George Hospital (KGH)",
            city = "Visakhapatnam",
            state = "Andhra Pradesh",
            type = "Government Tertiary Teaching Hospital",
            opHours = "08:00 AM - 02:00 PM (Mon-Sat)",
            opConsultationFee = "Free / Subsidized OP",
            officialOpPortalUrl = "https://dme.ap.nic.in/",
            emergencyPhone = "0891-2564891",
            departments = listOf(
                "General Medicine OP",
                "General Surgery OP",
                "Orthopedics OP",
                "Pediatrics OP",
                "Cardiology OP",
                "Pulmonology OP",
                "Dermatology OP"
            )
        )
    )
}
