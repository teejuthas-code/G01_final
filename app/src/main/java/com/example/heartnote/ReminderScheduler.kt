package com.example.heartnote

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.*

object ReminderScheduler {

    fun scheduleNotification(
        context: Context,
        title: String,
        reminderTime: String
    ) {

        try {

            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val date = format.parse(reminderTime)

            val intent = Intent(context, ReminderReceiver::class.java)
            intent.putExtra("task_text", title)

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                System.currentTimeMillis().toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val alarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                date!!.time,
                pendingIntent
            )

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}