package com.booktracker.lectio.domain.usecase

import com.booktracker.lectio.domain.repository.GenreRepository


class DeleteAllGenresUseCase(private val genreRepository: GenreRepository) {
    suspend operator fun invoke() {
        genreRepository.deleteAllGenres()
    }
}