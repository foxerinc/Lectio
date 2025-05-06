package com.booktracker.lectio.utils

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.booktracker.lectio.worker.ReadingReminderWorker
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    fun scheduleReadingReminder(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<ReadingReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(1, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "reading_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    fun cancelReadingReminder(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork("reading_reminder")
    }
}