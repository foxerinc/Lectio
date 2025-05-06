package com.booktracker.lectio.domain.usecase

import com.booktracker.lectio.domain.repository.BookRepository

class GetBookWithGenresByIdUseCase(private val repository: BookRepository) {
    suspend operator fun invoke(bookId: Int) = repository.getBookWithGenresById(bookId)
}