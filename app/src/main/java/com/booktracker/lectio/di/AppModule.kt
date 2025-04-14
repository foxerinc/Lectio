package com.booktracker.lectio.di

import android.content.Context
import androidx.room.Room
import com.booktracker.lectio.data.datasource.local.database.LectioDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LectioDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            LectioDatabase::class.java,
            "lectio_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }




}