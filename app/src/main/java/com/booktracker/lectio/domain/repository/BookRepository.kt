package com.booktracker.lectio.domain.repository


import com.booktracker.lectio.domain.model.Book
import com.booktracker.lectio.domain.model.BookWithGenres
import com.booktracker.lectio.utilis.BookStatusType
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getAllBooks(): Flow<List<Book>>
    fun getBooksByStatus(status: BookStatusType): Flow<List<BookWithGenres>>
    suspend fun insertBook(book: Book)
    suspend fun updateBook(book: Book)
    suspend fun deleteBook(book: Book)
    suspend fun getBookById(id: Int): Book?
    suspend fun updateCurrentPage(bookId: Int, page: Int)
    suspend fun updateFavorite(bookId: Int, favorite: Boolean)
}