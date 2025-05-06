package com.booktracker.lectio.domain.repository


import com.booktracker.lectio.domain.model.Book
import com.booktracker.lectio.domain.model.BookWithGenres
import com.booktracker.lectio.utils.BookStatusType
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getAllBooks(): Flow<List<BookWithGenres>>
    fun getBooksByStatus(status: BookStatusType?): Flow<List<BookWithGenres>>
    fun getFavoriteBooks(): Flow<List<BookWithGenres>>
    fun getBookWithGenresById(id: Int): Flow<BookWithGenres>
    suspend fun insertBook(book: Book) : Long
    suspend fun updateBook(book: Book)
    suspend fun deleteBook(book: Book)
    suspend fun deleteAllBooks()
    suspend fun updateCurrentPage(bookId: Int, page: Int)
    suspend fun updateFavorite(bookId: Int, favorite: Boolean)
}