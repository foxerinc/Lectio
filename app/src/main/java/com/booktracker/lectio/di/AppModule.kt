package com.booktracker.lectio.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.booktracker.lectio.data.datasource.local.database.LectioDatabase
import com.booktracker.lectio.data.repository.BookRepositoryImpl
import com.booktracker.lectio.data.repository.GenreRepositoryImpl
import com.booktracker.lectio.domain.repository.BookRepository
import com.booktracker.lectio.domain.repository.GenreRepository
import com.booktracker.lectio.domain.usecase.AddBookWithGenreUseCase
import com.booktracker.lectio.domain.usecase.BookUseCases
import com.booktracker.lectio.domain.usecase.DeleteAllBookUseCase
import com.booktracker.lectio.domain.usecase.DeleteAllGenresUseCase
import com.booktracker.lectio.domain.usecase.DeleteBookUseCase
import com.booktracker.lectio.domain.usecase.GenreUseCases
import com.booktracker.lectio.domain.usecase.GetAllBookUseCase
import com.booktracker.lectio.domain.usecase.GetAllGenresUseCase
import com.booktracker.lectio.domain.usecase.GetBookWithGenresByIdUseCase
import com.booktracker.lectio.domain.usecase.GetBooksByStatusUseCase
import com.booktracker.lectio.domain.usecase.GetFavoriteBookUseCase
import com.booktracker.lectio.domain.usecase.InsertGenresUseCase
import com.booktracker.lectio.domain.usecase.UpdateBookFavoriteStatusUseCase
import com.booktracker.lectio.domain.usecase.UpdateBookWithGenreUseCase
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
    fun provideBookUseCases(bookRepository: BookRepository, genreRepository: GenreRepository): BookUseCases {
        return BookUseCases(
            getBooksByStatusUseCase = GetBooksByStatusUseCase(bookRepository),
            addBookWithGenreUseCase = AddBookWithGenreUseCase(bookRepository, genreRepository),
            getAllBookUseCase = GetAllBookUseCase(bookRepository),
            getFavoriteBookUseCase = GetFavoriteBookUseCase(bookRepository),
            getBookWithGenresByIdUseCase = GetBookWithGenresByIdUseCase(bookRepository),
            updateBookUseCase = UpdateBookWithGenreUseCase(bookRepository,genreRepository),
            deleteBookUseCase = DeleteBookUseCase(bookRepository),
            updateBookFavoriteStatusUseCase = UpdateBookFavoriteStatusUseCase(bookRepository),
            deleteAllBookUseCase = DeleteAllBookUseCase(bookRepository)
        )
    }

    @Provides
    @Singleton
    fun provideGenreUseCases(genreRepository: GenreRepository): GenreUseCases {
        return GenreUseCases(
            getGenresUseCase = GetAllGenresUseCase(genreRepository),
            insertGenresUseCase = InsertGenresUseCase(genreRepository),
            deleteAllGenresUseCase = DeleteAllGenresUseCase(genreRepository)
        )
    }


    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }




}