package com.booktracker.lectio.domain.usecase

import com.booktracker.lectio.domain.model.Genre
import com.booktracker.lectio.domain.repository.GenreRepository
import kotlinx.coroutines.flow.Flow

class GetAllGenresUseCase(
    private val repository: GenreRepository
) {
    operator fun invoke(): Flow<List<Genre>> = repository.getAllGenres()
}