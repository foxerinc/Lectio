package com.booktracker.lectio.data.repository

import com.booktracker.lectio.data.datasource.local.database.LectioDatabase
import com.booktracker.lectio.data.datasource.local.entity.BookGenreCrossRef
import com.booktracker.lectio.domain.model.Genre
import com.booktracker.lectio.domain.repository.GenreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import com.booktracker.lectio.data.mapper.toDomain
import com.booktracker.lectio.data.mapper.toEntity

@Singleton
class GenreRepositoryImpl @Inject constructor(
    private val appDatabase: LectioDatabase
): GenreRepository {

    override fun getAllGenres(): Flow<List<Genre>> {
        return appDatabase.genreDao.getAllGenres().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun insertGenres(genres: List<Genre>) {
        appDatabase.genreDao.insertGenres(genres.map { it.toEntity() })
    }

    override suspend fun insertGenre(genre: Genre): Long {
        return appDatabase.genreDao.insertGenre(genre.toEntity())
    }

    override suspend fun updateGenreCrossRef(bookId: Int,genreId: Int) {
        appDatabase.genreDao.updateGenreCrossRef(bookId,genreId)
    }

    override suspend fun getGenreById(id: Int): Genre? {
        return appDatabase.genreDao.getGenreById(id)?.toDomain()
    }

    override suspend fun getGenreByName(name: String): Genre? {
        return appDatabase.genreDao.getGenreByName(name)?.toDomain()
    }

    override suspend fun assignGenreToBook(bookId: Int, genreId: Int) {
        appDatabase.genreDao.insertBookGenreCrossRef(
            BookGenreCrossRef(bookId = bookId, genreId = genreId)
        )
    }

    override suspend fun clearGenresForBook(bookId: Int) {
        appDatabase.genreDao.deleteGenresByBook(bookId)
    }
}
