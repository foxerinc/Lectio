package com.booktracker.lectio.data.mapper
import com.booktracker.lectio.data.datasource.local.entity.BookWithGenresEntity as BookWithGenresEntity
import com.booktracker.lectio.domain.model.BookWithGenres

fun BookWithGenresEntity.toDomain(): BookWithGenres {
    return BookWithGenres(
        book = bookEntity.toDomain(),
        genres = genreEntities.map { it.toDomain() }
    )
}