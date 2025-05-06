package com.booktracker.lectio.data.repository

import android.util.Log
import com.booktracker.lectio.data.datasource.local.database.LectioDatabase

import com.booktracker.lectio.data.mapper.toDomain
import com.booktracker.lectio.data.mapper.toEntity
import com.booktracker.lectio.domain.model.Book
import com.booktracker.lectio.domain.model.BookWithGenres
import com.booktracker.lectio.domain.repository.BookRepository
import com.booktracker.lectio.utils.BookStatusType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
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

    override fun getFavoriteBooks(): Flow<List<BookWithGenres>> {
        return appDatabase.bookDao.getFavoriteBooks().map { list ->
            list.map { it.toDomain() }
        }
    }

    override fun getBookWithGenresById(id: Int): Flow<BookWithGenres> {
        return appDatabase.bookDao.getBookWithGenresById(id).mapNotNull { it?.toDomain() }
    }

    override suspend fun insertBook(book: Book): Long {
        val entity = book.toEntity()
        return appDatabase.bookDao.insertBook(entity)
    }

    override suspend fun updateBook(book: Book) {
        val entity = book.toEntity()
        Log.d("BookRepositoryImpl", "Updating book with ID: ${book.id} and entity: $entity")
        appDatabase.bookDao.updateBook(entity)
        Log.d("BookRepositoryImpl", "Book updated in bookDao")
    }

    override suspend fun deleteBook(book: Book) {
        val entity = book.toEntity()
        appDatabase.bookDao.deleteBook(entity)
    }

    override suspend fun deleteAllBooks() {
        appDatabase.bookDao.deleteAllBooks()
    }


    override suspend fun updateCurrentPage(bookId: Int, page: Int) {
        appDatabase.bookDao.updateCurrentPage(bookId, page)
    }

    override suspend fun updateFavorite(bookId: Int, favorite: Boolean) {
        appDatabase.bookDao.updateFavorite(bookId, favorite)
    }
}