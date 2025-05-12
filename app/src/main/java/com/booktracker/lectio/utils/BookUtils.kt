package com.booktracker.lectio.utils

import com.booktracker.lectio.domain.model.Genre

object BookUtils {
    fun validateBookForm(title: String,
                         author: String,
                         status: BookStatusType,
                         currentPage: Int,
                         totalPage: Int,
                         notes: String,
                         personalRating: Float,
                         selectedGenre: List<Genre>): Pair<Boolean, BookFormErrors> {

        val errors = BookFormErrors()
        var isValid = true

        if (title.isBlank()) {
            errors.title = "Title cannot be empty"
            isValid = false
        }

        if (author.isBlank()) {
            errors.author = "Author cannot be empty"
            isValid = false
        }

        if (selectedGenre.isEmpty()) {
            errors.genres = "At least one genre is required"
            isValid = false
        }

        if (totalPage <= 0) {
            errors.totalPages = "Total pages must be a positive number"
            isValid = false
        }

        if (status == BookStatusType.FINISHED_READING) {
            if (notes.isBlank()) {
                errors.notes = "Notes are required for finished books"
                isValid = false
            }
            if (personalRating <= 0f) {
                errors.rating = "Personal rating is required for finished books"
                isValid = false
            }
        }

        if (status == BookStatusType.CURRENTLY_READING && (currentPage < 0 || currentPage > totalPage)) {
            errors.currentPage = "Current page must be between 0 and $totalPage"
            isValid = false
        }

        return Pair(isValid, errors)

    }

    fun adjustedCurrentPage(status: BookStatusType,currentPage: Int, totalPage: Int): Int {
        return when (status) {
            BookStatusType.WANT_TO_READ -> 0
            BookStatusType.FINISHED_READING -> totalPage
            BookStatusType.CURRENTLY_READING -> currentPage
        }

    }

    fun validateAndAdjustedGenres(selectedGenres: List<Genre>): List<Genre> {
        return selectedGenres
            .map { genre ->
                val cleanedName = genre.name.trim()
                    .split(" ")
                    .joinToString(" ") { word ->
                        word.split("-")
                            .joinToString("-") { part -> part.replaceFirstChar { it.uppercase() } }
                    }
                Genre(0,cleanedName)
            }
            .distinctBy { it.name.lowercase() }

    }

}