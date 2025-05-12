package com.booktracker.lectio.ui.screens.addBook

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booktracker.lectio.domain.model.Book
import com.booktracker.lectio.domain.model.Genre
import com.booktracker.lectio.domain.usecase.BookUseCases
import com.booktracker.lectio.domain.usecase.GenreUseCases
import com.booktracker.lectio.utils.BookStatusType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

import androidx.compose.runtime.State
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class AddBookViewModel @Inject constructor(private val bookUseCases: BookUseCases, private val genreUseCases: GenreUseCases)
    : ViewModel() {

    private val _isBookAdded = mutableStateOf(false)
    val isBookAdded: State<Boolean> = _isBookAdded

    private val _genres = mutableStateOf<List<Genre>>(emptyList())
    val genres: State<List<Genre>> = _genres

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


    init {
        fetchGenres()
    }

    private fun fetchGenres() {
        genreUseCases.getGenresUseCase().onEach { fetchedGenres ->
            if (fetchedGenres.isEmpty()) {
                val defaultGenres = listOf(
                    Genre(id = 0, name = "Fiction"),
                    Genre(id = 0, name = "Non-Fiction"),
                    Genre(id = 0, name = "Fantasy"),
                    Genre(id = 0, name = "Sci-Fi"),
                    Genre(id = 0, name = "Mystery"),
                    Genre(id = 0, name = "Romance")
                )
                _genres.value = defaultGenres
                genreUseCases.insertGenresUseCase(defaultGenres)
            } else {
                _genres.value = fetchedGenres
            }
        }.launchIn(viewModelScope)
    }

    fun addBook(
        title: String,
        author: String,
        description: String,
        coverImageUri: String,
        status: BookStatusType,
        currentPage: Int,
        totalPage: Int,
        notes: String,
        personalRating: Float,
        selectedGenre: List<Genre>
    ) {

        if (validateForm(title, author, status, currentPage, totalPage, notes, personalRating, selectedGenre)) return

        // Adjust currentPage based on status
        val adjustedCurrentPage = when (status) {
            BookStatusType.WANT_TO_READ -> 0
            BookStatusType.FINISHED_READING -> totalPage
            BookStatusType.CURRENTLY_READING -> currentPage
        }

        val newBook = Book(
            id = 0,
            title = title,
            author = author,
            description = description,
            coverImageUri = coverImageUri.ifBlank { null },
            status = status,
            currentPage = adjustedCurrentPage,
            totalPage = totalPage,
            notes = notes,
            personalRating = personalRating,
            isFavorite = false,
            bookAddedInMillis = 0L,
        )

        viewModelScope.launch {
            try {
                bookUseCases.addBookWithGenreUseCase(newBook, selectedGenre)
                _isBookAdded.value = true
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add book: ${e.message}"
            }
        }
    }

    private fun validateForm(title: String,
                             author: String,
                             status: BookStatusType,
                             currentPage: Int,
                             totalPage: Int,
                             notes: String,
                             personalRating: Float,
                             selectedGenre: List<Genre>) : Boolean {
        var isValid = true

        titleError.value = null
        authorError.value = null
        genresError.value = null
        totalPagesError.value = null
        currentPageError.value = null
        notesError.value = null
        ratingError.value = null

        // Validation
        if (title.isBlank()) {
            titleError.value = "Title cannot be empty"
            isValid = false
        }

        if (author.isBlank()) {
            authorError.value = "Author cannot be empty"
            isValid = false
        }

        if (selectedGenre.isEmpty()) {
            genresError.value = "At least one genre is required"
            isValid = false
        }


        if (totalPage <= 0) {
            totalPagesError.value = "Total pages must be a positive number"
            isValid = false
        }

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
        if (status == BookStatusType.CURRENTLY_READING && (currentPage < 0 || currentPage > totalPage)) {
            currentPageError.value = "Current page must be between 0 and $totalPage"
            isValid = false
        }

        if (!isValid) {
            _errorMessage.value = "All required fields need to be filled"
            return true
        }

        return false
    }

    fun addNewGenre(genreName: String) {
        if (genreName.isBlank()) return
        val newGenre = Genre(id = 0,name = genreName)
        _genres.value += newGenre
    }

    fun resetState() {
        _isBookAdded.value = false
        _errorMessage.value = null
    }
}