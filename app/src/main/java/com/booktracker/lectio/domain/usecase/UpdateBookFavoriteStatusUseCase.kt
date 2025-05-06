package com.booktracker.lectio.domain.usecase

import com.booktracker.lectio.domain.repository.BookRepository

class UpdateBookFavoriteStatusUseCase(private val bookRepository: BookRepository) {
    suspend operator fun invoke(bookId: Int, favorite: Boolean) {
        bookRepository.updateFavorite(bookId, favorite)
    }

}