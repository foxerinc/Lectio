package com.booktracker.lectio.domain.usecase

import com.booktracker.lectio.domain.model.BookWithGenres
import com.booktracker.lectio.domain.repository.BookRepository
import com.booktracker.lectio.utils.BookStatusType
import kotlinx.coroutines.flow.Flow

class GetAllBookUseCase (private val repository: BookRepository) {
    operator fun invoke(): Flow<List<BookWithGenres>> {
        return repository.getAllBooks()
    }
}