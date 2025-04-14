package com.booktracker.lectio.data.datasource.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.booktracker.lectio.data.datasource.local.dao.BookDao
import com.booktracker.lectio.data.datasource.local.dao.GenreDao
import com.booktracker.lectio.data.datasource.local.entity.BookEntity
import com.booktracker.lectio.data.datasource.local.entity.BookGenreCrossRef
import com.booktracker.lectio.data.datasource.local.entity.GenreEntity
import com.booktracker.lectio.utilis.BookStatusTypeConverter

@Database(
    entities = [BookEntity::class, GenreEntity::class, BookGenreCrossRef::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(BookStatusTypeConverter::class)
abstract class LectioDatabase: RoomDatabase() {
    abstract val bookDao: BookDao
    abstract val genreDao: GenreDao
}