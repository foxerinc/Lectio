package com.booktracker.lectio.domain.usecase

import com.booktracker.lectio.domain.model.Book
import com.booktracker.lectio.domain.model.Genre
import com.booktracker.lectio.domain.repository.BookRepository
import com.booktracker.lectio.domain.repository.GenreRepository

class UpdateBookWithGenreUseCase(private val bookRepository: BookRepository, private val genreRepository: GenreRepository) {
    suspend operator fun invoke(book: Book, genres: List<Genre>): Result<Unit> {
        return try {
            bookRepository.updateBook(book)
            genreRepository.clearGenresForBook(book.id)
            genres.forEach { genre ->
                val existingGenre = genreRepository.getGenreByName(genre.name)
                val genreId = existingGenre?.id ?: genreRepository.insertGenre(genre)
                genreRepository.assignGenreToBook(book.id, genreId.toInt())
            }
            Result.success(Unit)
        }catch (e: Exception) {
            Result.failure(e)
        }
    }
}