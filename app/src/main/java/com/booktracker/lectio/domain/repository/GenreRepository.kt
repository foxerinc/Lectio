package com.booktracker.lectio.domain.repository

import com.booktracker.lectio.domain.model.Genre
import kotlinx.coroutines.flow.Flow

interface GenreRepository {
    fun getAllGenres(): Flow<List<Genre>>
    suspend fun insertGenre(genre: Genre): Long
    suspend fun insertGenres(genres: List<Genre>)
    suspend fun updateGenreCrossRef(bookId: Int,genreId: Int)
    suspend fun getGenreById(id: Int): Genre?
    suspend fun getGenreByName(name: String): Genre?
    suspend fun assignGenreToBook(bookId: Int, genreId: Int)
    suspend fun clearGenresForBook(bookId: Int)
}