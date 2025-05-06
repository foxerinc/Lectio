package com.booktracker.lectio.domain.usecase

import com.booktracker.lectio.domain.model.Genre
import com.booktracker.lectio.domain.repository.GenreRepository
import javax.inject.Inject

class UpdateGenreCrossRefUseCase @Inject constructor(private val genreRepository: GenreRepository) {
    suspend operator fun invoke(bookId: Int, genres: List<Genre>) {
        genres.forEach{ genre ->
            val existingGenre = genreRepository.getGenreByName(genre.name)
            val genreId = existingGenre?.id ?: genreRepository.insertGenre(genre)
            genreRepository.updateGenreCrossRef(bookId, genreId.toInt())
        }

    }
}