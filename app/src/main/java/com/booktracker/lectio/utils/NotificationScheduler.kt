package com.booktracker.lectio.utils

import android.content.Context
import android.util.Log
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.booktracker.lectio.worker.ReadingReminderWorker
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import java.time.Duration

object NotificationScheduler {

    fun scheduleReadingReminder(context: Context) {
        Log.d("NotificationScheduler", "scheduleReadingReminder() called")

        val now = LocalDateTime.now()
        val targetTime = now.withHour(19).withMinute(0).withSecond(0).withNano(0)

        val delayMinutes = if (now.isBefore(targetTime)) {
            Duration.between(now, targetTime).toMinutes()
        } else {
            Duration.between(now, targetTime.plusDays(1)).toMinutes()
        }


        val workRequest = PeriodicWorkRequestBuilder<ReadingReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(delayMinutes, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "reading_reminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )

        Log.d("NotificationScheduler", "Scheduled to run in $delayMinutes minutes")
    }

    fun cancelReadingReminder(context: Context) {
        Log.d("NotificationScheduler", "cancelReadingReminder() called")
        WorkManager.getInstance(context).cancelUniqueWork("reading_reminder")
    }
}