package com.booktracker.lectio.domain.model

import com.booktracker.lectio.utilis.BookStatusType

data class Book(
    val id: Int,
    val title: String,
    val author: String?,
    val description: String?,
    val coverImageUri: String?,
    val totalPage: Int,
    val currentPage: Int,
    val isFavorite: Boolean,
    val status: BookStatusType,
    val personalRating: Float? = null,
    val notes: String? = null
)