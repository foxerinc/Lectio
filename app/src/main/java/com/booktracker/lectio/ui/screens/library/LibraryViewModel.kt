package com.booktracker.lectio.ui.screens.library

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.booktracker.lectio.domain.model.BookWithGenres
import com.booktracker.lectio.domain.usecase.BookUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.booktracker.lectio.utils.BookStatusType
import com.booktracker.lectio.utils.SortOption
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val bookUseCases: BookUseCases
) : ViewModel() {

    private val _books = mutableStateOf<List<BookWithGenres>>(emptyList())
    val books: State<List<BookWithGenres>> = _books

    private val _filteredBooks = mutableStateOf<List<BookWithGenres>>(emptyList())
    val filteredBooks: State<List<BookWithGenres>> = _filteredBooks

    private val _searchQuery = mutableStateOf("")
    val searchQuery: State<String> = _searchQuery

    private val _selectedStatusFilter = mutableStateOf<BookStatusType?>(null)
    val selectedStatusFilter: State<BookStatusType?> = _selectedStatusFilter

    private val _sortOption = mutableStateOf(SortOption.DATE_ADDED)
    val sortOption: State<SortOption> = _sortOption

    init {
        fetchBooks()
    }

    private fun fetchBooks() {
        bookUseCases.getAllBookUseCase().onEach { books ->
            _books.value = books
            applyFiltersAndSorting()
        }.launchIn(viewModelScope)
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
        applyFiltersAndSorting()
    }

    fun onStatusFilterChange(status: BookStatusType?) {
        _selectedStatusFilter.value = status
        applyFiltersAndSorting()
    }

    fun onSortOptionChange(option: SortOption) {
        _sortOption.value = option
        applyFiltersAndSorting()
    }

    private fun applyFiltersAndSorting() {
        var filtered = _books.value

        // Apply search query filter
        if (_searchQuery.value.isNotBlank()) {
            val query = _searchQuery.value.lowercase()
            filtered = filtered.filter { bookWithGenres ->
                bookWithGenres.book.title.lowercase().contains(query) ||
                        (bookWithGenres.book.author?.lowercase()?.contains(query) ?: false)
            }
        }

        // Apply status filter
        _selectedStatusFilter.value?.let { status ->
            filtered = filtered.filter { it.book.status == status }
        }

        // Apply sorting
        filtered = when (_sortOption.value) {
            SortOption.TITLE -> filtered.sortedBy { it.book.title }
            SortOption.AUTHOR -> filtered.sortedBy { it.book.author }
            SortOption.RATING -> filtered.sortedByDescending { it.book.personalRating }
            SortOption.GENRE -> filtered.sortedBy { it.genres.firstOrNull()?.name ?: "" }
            SortOption.DATE_ADDED -> filtered.sortedByDescending { it.book.bookAddedInMillis }
        }

        _filteredBooks.value = filtered
    }
}