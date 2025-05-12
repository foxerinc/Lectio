package com.booktracker.lectio.utils

data class BookFormErrors(
    var title: String? = null,
    var author: String? = null,
    var genres: String? = null,
    var totalPages: String? = null,
    var currentPage: String? = null,
    var notes: String? = null,
    var rating: String? = null,
)