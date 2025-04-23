package com.booktracker.lectio.data.repository

import com.booktracker.lectio.data.datasource.local.database.LectioDatabase

import com.booktracker.lectio.data.mapper.toDomain
import com.booktracker.lectio.data.mapper.toEntity
import com.booktracker.lectio.domain.model.Book
import com.booktracker.lectio.domain.model.BookWithGenres
import com.booktracker.lectio.domain.repository.BookRepository
import com.booktracker.lectio.utilis.BookStatusType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BookRepositoryImpl @Inject constructor(
    private val appDatabase: LectioDatabase
) : BookRepository {

    override fun getAllBooks(): Flow<List<BookWithGenres>> {
        return appDatabase.bookDao.getBooksWithGenres().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getBooksByStatus(status: BookStatusType?): Flow<List<BookWithGenres>> {
        return if (status == null){
            getAllBooks()
        }else{
            appDatabase.bookDao.getListOfBookByReadingStatus(status).map { list ->
                list.map { it.toDomain() }
            }
        }

    }

    override suspend fun insertBook(book: Book) {
        val entity = book.toEntity()
        appDatabase.bookDao.insertBook(entity)
    }

    override suspend fun updateBook(book: Book) {
        val entity = book.toEntity()
        appDatabase.bookDao.updateBook(entity)
    }

    override suspend fun deleteBook(book: Book) {
        val entity = book.toEntity()
        appDatabase.bookDao.deleteBook(entity)
    }

    override suspend fun getBookById(id: Int): Book? {
        return appDatabase.bookDao.getBookById(id)?.toDomain()
    }

    override suspend fun updateCurrentPage(bookId: Int, page: Int) {
        appDatabase.bookDao.updateCurrentPage(bookId, page)
    }

    override suspend fun updateFavorite(bookId: Int, favorite: Boolean) {
        appDatabase.bookDao.updateFavorite(bookId, favorite)
    }
}