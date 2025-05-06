package com.booktracker.lectio.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.booktracker.lectio.MainActivity
import com.booktracker.lectio.R
import com.booktracker.lectio.domain.usecase.BookUseCases
import com.booktracker.lectio.utils.BookStatusType
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first


@HiltWorker
class ReadingReminderWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    @Assisted private val bookUseCases: BookUseCases
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val CHANNEL_ID = "reading_reminder_channel"
        const val NOTIFICATION_ID = 1
    }

    override suspend fun doWork(): Result {
        // Fetch Book With Currently Reading Status
        val currentlyReadingBooks = bookUseCases.getBooksByStatusUseCase(BookStatusType.CURRENTLY_READING).first()
        if (currentlyReadingBooks.isEmpty()) {
            return Result.success()
        }

        val totalOfCurrentlyReadingBooks = currentlyReadingBooks.size
        // Select the book with the highest progress percentage
        val bookWithHighestProgress = currentlyReadingBooks.maxByOrNull { bookWithGenres ->
            if (bookWithGenres.book.totalPage > 0) {
                (bookWithGenres.book.currentPage.toFloat() / bookWithGenres.book.totalPage) * 100
            } else {
                0f
            }
        } ?: currentlyReadingBooks.first()

        val progress = if (bookWithHighestProgress.book.totalPage > 0) {
            ((bookWithHighestProgress.book.currentPage.toFloat() / bookWithHighestProgress.book.totalPage) * 100).toInt()
        } else {
            0f

        }

        val title = "You have $totalOfCurrentlyReadingBooks currently reading book"
        val message = if (totalOfCurrentlyReadingBooks > 1){
            "Time to Read: ${bookWithHighestProgress.book.title} - You're $progress% through '${bookWithHighestProgress.book.title}. Keep going!"
        }else{
            "Come back to the app to add some book to read!"
        }

        // Create a PendingIntent for the notification
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            intent,
            pendingIntentFlags
        )

        // Create notification channel (required for Android 8.0+)
        createNotificationChannel()

        // Build the notification
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()


        // Show the notification
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)

        return Result.success()


    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Reading Reminders"
            val descriptionText = "Notifications to remind you to continue reading your books"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}