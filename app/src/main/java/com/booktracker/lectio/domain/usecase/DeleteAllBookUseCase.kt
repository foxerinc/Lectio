package com.booktracker.lectio.domain.usecase

import com.booktracker.lectio.domain.repository.BookRepository
import javax.inject.Inject

class DeleteAllBookUseCase @Inject constructor(private val bookRepository: BookRepository) {
    suspend operator fun invoke() {
        bookRepository.deleteAllBooks()
    }
}