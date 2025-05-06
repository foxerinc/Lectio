package com.booktracker.lectio.di

import android.app.Application
import android.content.Context
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.booktracker.lectio.domain.usecase.BookUseCases
import com.booktracker.lectio.worker.ReadingReminderWorker
import javax.inject.Inject

@HiltAndroidApp
class LectioApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: AppWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        Log.d("WorkManagerConfig", "HiltWorkerFactory digunakan")
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

}

class AppWorkerFactory @Inject constructor(private val bookUseCases: BookUseCases) : WorkerFactory(){
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when(workerClassName){
            ReadingReminderWorker::class.java.name -> ReadingReminderWorker(appContext,workerParameters,bookUseCases)
            else -> null
        }
    }
}