package com.example.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.data.model.MedicationReminder
import java.util.Calendar

object MedicationScheduler {

    fun scheduleMedicationAlarm(context: Context, reminder: MedicationReminder) {
        if (!reminder.isActive) {
            cancelMedicationAlarm(context, reminder.id)
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val intent = Intent(context, MedicationAlarmReceiver::class.java).apply {
            action = MedicationNotificationHelper.ACTION_MEDICATION_ALARM
            putExtra(MedicationNotificationHelper.EXTRA_REMINDER_ID, reminder.id)
            putExtra(MedicationNotificationHelper.EXTRA_MEDICINE_NAME, reminder.medicineName)
            putExtra(MedicationNotificationHelper.EXTRA_DOSAGE, reminder.dosage)
            putExtra(MedicationNotificationHelper.EXTRA_INSTRUCTION, reminder.instruction)
            putExtra(MedicationNotificationHelper.EXTRA_CATEGORY, reminder.category)
        }

        val requestCode = (reminder.id % 100000).toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Calculate target trigger time
        val targetCal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, reminder.timeHour)
            set(Calendar.MINUTE, reminder.timeMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // If time has already passed today, schedule for tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    targetCal.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    targetCal.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            // Fallback for API 31+ exact alarm permission
            try {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    targetCal.timeInMillis,
                    pendingIntent
                )
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun cancelMedicationAlarm(context: Context, reminderId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, MedicationAlarmReceiver::class.java).apply {
            action = MedicationNotificationHelper.ACTION_MEDICATION_ALARM
            putExtra(MedicationNotificationHelper.EXTRA_REMINDER_ID, reminderId)
        }
        val requestCode = (reminderId % 100000).toInt()
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    fun scheduleSnoozeAlarm(context: Context, reminderId: Long, medicineName: String, dosage: String, instruction: String, category: String, minutes: Int = 10) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, MedicationAlarmReceiver::class.java).apply {
            action = MedicationNotificationHelper.ACTION_MEDICATION_ALARM
            putExtra(MedicationNotificationHelper.EXTRA_REMINDER_ID, reminderId)
            putExtra(MedicationNotificationHelper.EXTRA_MEDICINE_NAME, medicineName)
            putExtra(MedicationNotificationHelper.EXTRA_DOSAGE, dosage)
            putExtra(MedicationNotificationHelper.EXTRA_INSTRUCTION, instruction)
            putExtra(MedicationNotificationHelper.EXTRA_CATEGORY, category)
        }
        val requestCode = (reminderId % 100000).toInt() + 50000
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerAt = System.currentTimeMillis() + (minutes * 60 * 1000L)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun triggerInstantTestNotification(context: Context, reminder: MedicationReminder) {
        MedicationNotificationHelper.showMedicationNotification(
            context = context,
            reminderId = reminder.id,
            medicineName = reminder.medicineName,
            dosage = reminder.dosage,
            instruction = reminder.instruction,
            category = reminder.category
        )
    }
}
