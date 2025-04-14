package com.booktracker.lectio.di

import android.content.Context
import androidx.room.Room
import com.booktracker.lectio.data.datasource.local.database.LectioDatabase
import com.booktracker.lectio.data.repository.BookRepositoryImpl
import com.booktracker.lectio.data.repository.GenreRepositoryImpl
import com.booktracker.lectio.domain.repository.BookRepository
import com.booktracker.lectio.domain.repository.GenreRepository
import com.booktracker.lectio.domain.usecase.BookUseCases
import com.booktracker.lectio.domain.usecase.GetCurrentlyReadingBooksUseCase
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

    @Provides
    @Singleton
    fun provideBookRepository(database: LectioDatabase): BookRepository {
        return BookRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideGenreRepository(database: LectioDatabase): GenreRepository {
        return GenreRepositoryImpl(database)
    }

    @Provides
    @Singleton
    fun provideBookUseCases(repository: BookRepository): BookUseCases {
        return BookUseCases(
            getCurrentlyReadingBooks = GetCurrentlyReadingBooksUseCase(repository),

        )
    }



}