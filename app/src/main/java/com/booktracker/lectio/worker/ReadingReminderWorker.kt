package com.booktracker.lectio.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
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
        Log.d("ReadingReminderWorker", "doWork() called")
        // Fetch Book With Currently Reading Status
        val currentlyReadingBooks = bookUseCases.getBooksByStatusUseCase(BookStatusType.CURRENTLY_READING).first()

        val notifTittle: String
        val notifMessage: String

        if (currentlyReadingBooks.isEmpty()) {
            notifTittle = "You have 0 currently reading book"
            notifMessage = "Come back to the app to add some book to read!"
        }else{
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
            notifTittle = "You have $totalOfCurrentlyReadingBooks currently reading book"
            notifMessage = "Time to Read: ${bookWithHighestProgress.book.title} - You're $progress% through '${bookWithHighestProgress.book.title}. Keep going!"


        }


        // Create a PendingIntent for the notification
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }


        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )


        createNotificationChannel()

        // Build the notification
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(notifTittle)
            .setContentText(notifMessage)
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