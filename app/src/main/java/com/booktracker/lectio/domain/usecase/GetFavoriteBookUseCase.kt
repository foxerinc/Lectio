package com.booktracker.lectio.domain.usecase

import com.booktracker.lectio.domain.repository.BookRepository

class GetFavoriteBookUseCase(private val bookRepository: BookRepository) {
    operator fun invoke() = bookRepository.getFavoriteBooks()
}