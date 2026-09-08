package com.example.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MedicationAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return

        val reminderId = intent.getLongExtra(MedicationNotificationHelper.EXTRA_REMINDER_ID, 0L)
        val medicineName = intent.getStringExtra(MedicationNotificationHelper.EXTRA_MEDICINE_NAME) ?: "Prescribed Medicine"
        val dosage = intent.getStringExtra(MedicationNotificationHelper.EXTRA_DOSAGE) ?: "1 Dose"
        val instruction = intent.getStringExtra(MedicationNotificationHelper.EXTRA_INSTRUCTION) ?: "As prescribed"
        val category = intent.getStringExtra(MedicationNotificationHelper.EXTRA_CATEGORY) ?: "Medication"

        when (intent.action) {
            MedicationNotificationHelper.ACTION_MEDICATION_ALARM -> {
                // Show notification alert
                MedicationNotificationHelper.showMedicationNotification(
                    context = context,
                    reminderId = reminderId,
                    medicineName = medicineName,
                    dosage = dosage,
                    instruction = instruction,
                    category = category
                )

                // Reschedule for next day in Room
                if (reminderId > 0) {
                    val appContext = context.applicationContext
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val db = AppDatabase.getDatabase(appContext)
                            val reminder = db.medicationReminderDao().getReminderById(reminderId)
                            if (reminder != null && reminder.isActive) {
                                MedicationScheduler.scheduleMedicationAlarm(appContext, reminder)
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            }

            MedicationNotificationHelper.ACTION_MARK_TAKEN -> {
                // Dismiss notification
                MedicationNotificationHelper.dismissNotification(context, reminderId)

                // Mark as taken in Room DB
                val appContext = context.applicationContext
                val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val db = AppDatabase.getDatabase(appContext)
                        val reminder = db.medicationReminderDao().getReminderById(reminderId)
                        val newStreak = if (reminder != null) reminder.streakDays + 1 else 1
                        db.medicationReminderDao().markReminderTaken(reminderId, todayStr, newStreak)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                Toast.makeText(context, "✅ Marked $medicineName as taken!", Toast.LENGTH_SHORT).show()
            }

            MedicationNotificationHelper.ACTION_SNOOZE -> {
                // Dismiss notification & snooze for 10 minutes
                MedicationNotificationHelper.dismissNotification(context, reminderId)
                MedicationScheduler.scheduleSnoozeAlarm(
                    context = context,
                    reminderId = reminderId,
                    medicineName = medicineName,
                    dosage = dosage,
                    instruction = instruction,
                    category = category,
                    minutes = 10
                )
                Toast.makeText(context, "⏱️ Snoozed $medicineName for 10 minutes", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
