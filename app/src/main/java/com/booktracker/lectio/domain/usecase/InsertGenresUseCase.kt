package com.booktracker.lectio.domain.usecase

import com.booktracker.lectio.domain.model.Genre
import com.booktracker.lectio.domain.repository.GenreRepository
import javax.inject.Inject

class InsertGenresUseCase @Inject constructor(private val genreRepository: GenreRepository) {
    suspend operator fun invoke(genre: List<Genre>) {
        genreRepository.insertGenres(genre)
    }

}