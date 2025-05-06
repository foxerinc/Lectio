package com.booktracker.lectio.domain.usecase

import com.booktracker.lectio.domain.model.Book
import com.booktracker.lectio.domain.model.Genre
import com.booktracker.lectio.domain.repository.BookRepository
import com.booktracker.lectio.domain.repository.GenreRepository
import javax.inject.Inject


class AddBookWithGenreUseCase @Inject constructor(private val bookRepository: BookRepository, private val genreRepository: GenreRepository) {
    suspend operator fun invoke(book: Book, genres: List<Genre>) {
        val bookId = bookRepository.insertBook(book)
        genres.forEach { genre ->
            val existingGenre = genreRepository.getGenreByName(genre.name)
            val genreId =  existingGenre?.id ?: genreRepository.insertGenre(genre)
            genreRepository.assignGenreToBook(bookId.toInt(), genreId.toInt())
        }
    }
}