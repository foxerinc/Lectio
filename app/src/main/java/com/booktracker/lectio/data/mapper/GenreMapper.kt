package com.booktracker.lectio.data.mapper

import com.booktracker.lectio.data.datasource.local.entity.GenreEntity
import com.booktracker.lectio.domain.model.Genre

fun GenreEntity.toDomain(): Genre {
    return Genre(
        id = this.genreId,
        name = this.name
    )
}

fun Genre.toEntity(): GenreEntity {
    return GenreEntity(
        genreId = this.id,
        name = this.name
    )
}