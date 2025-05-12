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

        val (isValid, errors) = BookUtils.validateBookForm(
            title, author, status, currentPage, totalPage, notes, personalRating, selectedGenre
        )

        titleError.value = errors.title
        authorError.value = errors.author
        genresError.value = errors.genres
        totalPagesError.value = errors.totalPages
        currentPageError.value = errors.currentPage
        notesError.value = errors.notes
        ratingError.value = errors.rating

        if (!isValid) {
            _errorMessage.value = "All required fields need to be filled"
            return
        }

        // Adjust currentPage based on status
        val adjustedCurrentPage = BookUtils.adjustedCurrentPage(status, currentPage, totalPage)

        // Clean & normalized genres
        val cleanedGenres = BookUtils.validateAndAdjustedGenres(selectedGenre)

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
                    val update = bookUseCase.updateBookUseCase(book, cleanedGenres)
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


    fun clearErrorMessage() {
        _errorMessage.value = null
    }

}