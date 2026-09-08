package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R
import com.example.data.model.MedicationReminder

object MedicationNotificationHelper {
    const val CHANNEL_ID = "medication_reminders_channel"
    const val CHANNEL_NAME = "Medication & Prescription Reminders"
    const val CHANNEL_DESCRIPTION = "Timely alerts and reminders for your prescribed medicines and daily doses"

    const val ACTION_MEDICATION_ALARM = "com.example.notification.ACTION_MEDICATION_ALARM"
    const val ACTION_MARK_TAKEN = "com.example.notification.ACTION_MARK_TAKEN"
    const val ACTION_SNOOZE = "com.example.notification.ACTION_SNOOZE"

    const val EXTRA_REMINDER_ID = "extra_reminder_id"
    const val EXTRA_MEDICINE_NAME = "extra_medicine_name"
    const val EXTRA_DOSAGE = "extra_dosage"
    const val EXTRA_INSTRUCTION = "extra_instruction"
    const val EXTRA_CATEGORY = "extra_category"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .build()

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
                enableLights(true)
                lightColor = Color.parseColor("#0D9488")
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 400, 200, 400)
                setSound(soundUri, audioAttributes)
                setShowBadge(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showMedicationNotification(
        context: Context,
        reminderId: Long,
        medicineName: String,
        dosage: String,
        instruction: String,
        category: String
    ) {
        createNotificationChannel(context)

        val notificationId = (reminderId % 100000).toInt()

        // Main Tap Intent -> Open AarogyaCare App
        val appIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("OPEN_SCREEN", "MEDICATIONS")
            putExtra("HIGHLIGHT_REMINDER_ID", reminderId)
        }
        val appPendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            appIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action 1: Mark As Taken
        val takenIntent = Intent(context, MedicationAlarmReceiver::class.java).apply {
            action = ACTION_MARK_TAKEN
            putExtra(EXTRA_REMINDER_ID, reminderId)
            putExtra(EXTRA_MEDICINE_NAME, medicineName)
        }
        val takenPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId + 10000,
            takenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action 2: Snooze (10 min)
        val snoozeIntent = Intent(context, MedicationAlarmReceiver::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(EXTRA_REMINDER_ID, reminderId)
            putExtra(EXTRA_MEDICINE_NAME, medicineName)
            putExtra(EXTRA_DOSAGE, dosage)
            putExtra(EXTRA_INSTRUCTION, instruction)
            putExtra(EXTRA_CATEGORY, category)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId + 20000,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val bigTextStyle = NotificationCompat.BigTextStyle()
            .setBigContentTitle("⏰ Time for your medicine: $medicineName")
            .bigText("Dosage: $dosage\nTiming: $instruction\nCategory: $category\n\nPlease take your prescribed dose as advised by your doctor.")
            .setSummaryText("AarogyaCare Medication Reminder")

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("⏰ Time for your medicine: $medicineName")
            .setContentText("$dosage • $instruction")
            .setStyle(bigTextStyle)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setColor(Color.parseColor("#0D9488"))
            .setAutoCancel(true)
            .setContentIntent(appPendingIntent)
            .addAction(android.R.drawable.checkbox_on_background, "✅ Mark as Taken", takenPendingIntent)
            .addAction(android.R.drawable.ic_popup_reminder, "⏱️ Snooze 10m", snoozePendingIntent)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(notificationId, builder.build())
        } catch (e: SecurityException) {
            // Handled safely in case notification permission is pending
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun dismissNotification(context: Context, reminderId: Long) {
        val notificationId = (reminderId % 100000).toInt()
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.cancel(notificationId)
    }
}
