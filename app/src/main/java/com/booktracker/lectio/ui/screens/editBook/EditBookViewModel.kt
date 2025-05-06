package com.booktracker.lectio.ui.screens.editBook

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booktracker.lectio.domain.model.Book
import com.booktracker.lectio.domain.model.BookWithGenres
import com.booktracker.lectio.domain.model.Genre
import com.booktracker.lectio.domain.usecase.BookUseCases
import com.booktracker.lectio.domain.usecase.GenreUseCases
import com.booktracker.lectio.utils.BookStatusType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class EditBookViewModel @Inject constructor(private val bookUseCase: BookUseCases, private val genreUseCases: GenreUseCases) : ViewModel() {

    private val _bookWithGenres = MutableStateFlow<BookWithGenres?>(null)
    val bookWithGenres: StateFlow<BookWithGenres?> = _bookWithGenres

    private val _isBookUpdated = mutableStateOf(false)
    val isBookUpdated: State<Boolean> = _isBookUpdated

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Field-specific error states
    val titleError = mutableStateOf<String?>(null)
    val authorError = mutableStateOf<String?>(null)
    val genresError = mutableStateOf<String?>(null)
    val currentPageError = mutableStateOf<String?>(null)
    val totalPagesError = mutableStateOf<String?>(null)
    val notesError = mutableStateOf<String?>(null)
    val ratingError = mutableStateOf<String?>(null)

    fun loadBook(bookId: Int) {
        viewModelScope.launch {
            bookUseCase.getBookWithGenresByIdUseCase(bookId).collect { book ->
                book.let {
                    _bookWithGenres.value = book
                }
            }
        }
    }

    fun updateBook(
        bookId: Int,
        title: String,
        author: String,
        description: String,
        coverImageUri: String,
        status: BookStatusType,
        currentPage: Int,
        totalPage: Int,
        notes: String,
        personalRating: Float,
        selectedGenre: List<Genre>,
        isFavorite: Boolean,
    ) {

        if (validateForm(title, author, status, currentPage, totalPage, notes, personalRating, selectedGenre)) return

        val adjustedCurrentPage = when (status) {
            BookStatusType.WANT_TO_READ -> 0
            BookStatusType.FINISHED_READING -> totalPage
            BookStatusType.CURRENTLY_READING -> currentPage
        }

        viewModelScope.launch {
            try {
                val book = Book(
                    id = bookId,
                    title = title,
                    author = author,
                    description = description,
                    coverImageUri = coverImageUri,
                    status = status,
                    currentPage = adjustedCurrentPage,
                    totalPage = totalPage,
                    notes = notes,
                    personalRating = if (status == BookStatusType.FINISHED_READING) personalRating else null,
                    bookAddedInMillis = _bookWithGenres.value?.book?.bookAddedInMillis
                        ?: System.currentTimeMillis(),
                    isFavorite = isFavorite
                )
                withContext(Dispatchers.IO){
                    val update = bookUseCase.updateBookUseCase(book, selectedGenre)
                    if (update.isFailure) {
                        _errorMessage.value = update.exceptionOrNull()?.message
                        return@withContext
                    }
                }
                _isBookUpdated.value = true
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update book: ${e.message}"
            }
        }

    }

    private fun validateForm(
        title: String,
        author: String,
        status: BookStatusType,
        currentPage: Int,
        totalPage: Int,
        notes: String,
        personalRating: Float,
        selectedGenre: List<Genre>,
    ): Boolean {
        var isValid = true

        // Reset all error states
        titleError.value = null
        authorError.value = null
        genresError.value = null
        totalPagesError.value = null
        currentPageError.value = null
        notesError.value = null
        ratingError.value = null

        // Validate title
        if (title.isBlank()) {
            titleError.value = "Title is required"
            isValid = false
        }

        // Validate author
        if (author.isBlank()) {
            authorError.value = "Author is required"
            isValid = false
        }

        // Validate genres
        if (selectedGenre.isEmpty()) {
            genresError.value = "At least one genre is required"
            isValid = false
        }

        // Validate total pages
        if (totalPage <= 0) {
            totalPagesError.value = "Total pages must be a positive number"
            isValid = false
        }

        // Validate current page for CURRENTLY_READING status
        if (status == BookStatusType.CURRENTLY_READING && (currentPage < 0 || currentPage > totalPage)) {
            currentPageError.value = "Current page must be between 0 and $totalPage"
            isValid = false
        }

        // Validate notes and personal rating for FINISHED_READING status
        if (status == BookStatusType.FINISHED_READING) {
            if (notes.isBlank()) {
                notesError.value = "Notes are required for finished books"
                isValid = false
            }
            if (personalRating <= 0f) {
                ratingError.value = "Personal rating is required for finished books"
                isValid = false
            }
        }

        // If validation fails, show a static error message and return
        if (!isValid) {
            _errorMessage.value = "All required fields need to be filled"
            return true
        }

        return false
    }


    fun clearErrorMessage() {
        _errorMessage.value = null
    }

}