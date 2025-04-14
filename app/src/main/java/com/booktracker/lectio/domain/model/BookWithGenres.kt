package com.booktracker.lectio.domain.model

data class BookWithGenres(
    val book: Book,
    val genres: List<Genre>
)