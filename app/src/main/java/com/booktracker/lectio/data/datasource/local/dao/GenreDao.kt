package com.booktracker.lectio.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.booktracker.lectio.data.datasource.local.entity.BookGenreCrossRef
import com.booktracker.lectio.data.datasource.local.entity.GenreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGenre(genreEntity: GenreEntity): Long

    @Query("SELECT * FROM genres ORDER BY name ASC")
    fun getAllGenres(): Flow<List<GenreEntity>>

    @Transaction
    @Query("SELECT * FROM genres WHERE genre_id = :genreId")
    suspend fun getGenreById(genreId: Int): GenreEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookGenreCrossRef(crossRef: BookGenreCrossRef)

    @Query("DELETE FROM book_genre_cross_ref WHERE book_id = :bookId")
    suspend fun deleteGenresByBook(bookId: Int)
}
