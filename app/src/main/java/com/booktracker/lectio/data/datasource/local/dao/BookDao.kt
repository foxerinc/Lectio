package com.booktracker.lectio.data.datasource.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.booktracker.lectio.data.datasource.local.entity.BookEntity
import com.booktracker.lectio.data.datasource.local.entity.BookWithGenresEntity
import com.booktracker.lectio.domain.model.BookWithGenres
import com.booktracker.lectio.utilis.BookStatusType
import com.booktracker.lectio.utilis.CURRENTLY_READING_VALUE
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllBooks(vararg bookEntities: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(bookEntity: BookEntity) : Long

    @Transaction
    @Query("SELECT * FROM book WHERE status = :status ORDER BY bookAddedInMillis DESC")
    fun getListOfBookByReadingStatus(status: BookStatusType): Flow<List<BookWithGenresEntity>>


    @Query("SELECT * FROM book WHERE book_id = :id LIMIT 1")
    suspend fun getBookById(id: Int): BookEntity?

    @Transaction
    @Query("SELECT * FROM book ORDER BY bookAddedInMillis DESC")
    fun getBooksWithGenres(): Flow<List<BookWithGenresEntity>>

    @Transaction
    @Query("SELECT * FROM book WHERE book_id = :bookId ORDER BY bookAddedInMillis DESC")
    suspend fun getBookWithGenresById(bookId: Int): BookWithGenresEntity?

    @Query("SELECT COUNT(*) FROM book WHERE status = '$CURRENTLY_READING_VALUE' ORDER BY bookAddedInMillis DESC")
    fun getTotalOfCurrentlyReadBooks(): Int

    @Update
    fun updateBook(updatedBookEntity: BookEntity)

    @Query("UPDATE book SET currentPage = :page WHERE book_id = :bookId")
    suspend fun updateCurrentPage(bookId: Int, page: Int)

    @Query("UPDATE book SET isFavorite = :favorite WHERE book_id = :bookId")
    suspend fun updateFavorite(bookId: Int, favorite: Boolean)

    @Delete
    fun deleteBook(bookEntity: BookEntity)

}