package com.booktracker.lectio.domain.usecase

import com.booktracker.lectio.data.datasource.local.entity.BookEntity
import com.booktracker.lectio.domain.model.Book
import com.booktracker.lectio.domain.repository.BookRepository

class DeleteBookUseCase (private val repository: BookRepository) {
    suspend operator fun invoke(book: Book) {
        repository.deleteBook(book)
    }
}