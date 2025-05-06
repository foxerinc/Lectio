package com.booktracker.lectio.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booktracker.lectio.domain.model.BookWithGenres
import com.booktracker.lectio.domain.usecase.BookUseCases
import com.booktracker.lectio.utils.BookStatusType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val bookUseCases: BookUseCases) : ViewModel() {
    private val _currentlyReadingBooks = MutableStateFlow<List<BookWithGenres>>(emptyList())
    val currentlyReadingBooks: StateFlow<List<BookWithGenres>> = _currentlyReadingBooks

    private val _finishedBooksCount = MutableStateFlow(0)
    val finishedBooksCount: StateFlow<Int> = _finishedBooksCount

    private val _currentlyReadingBooksCount = MutableStateFlow(0)
    val currentlyReadingBooksCount: StateFlow<Int> = _currentlyReadingBooksCount

    private val _totalPagesRead = MutableStateFlow(0)
    val totalPagesRead: StateFlow<Int> = _totalPagesRead


    init {
        getCurrentlyReadingBooks()
        getFinishedReadingBooks()
        getAllBooks()
    }

    private fun getCurrentlyReadingBooks() {
        bookUseCases.getBooksByStatusUseCase(BookStatusType.CURRENTLY_READING)
            .onEach { books -> _currentlyReadingBooks.value = books
            _currentlyReadingBooksCount.value = books.size}
            .launchIn(viewModelScope)
    }

    private fun getFinishedReadingBooks(){
        bookUseCases.getBooksByStatusUseCase(BookStatusType.FINISHED_READING)
            .onEach { books -> _finishedBooksCount.value = books.size }
            .launchIn(viewModelScope)
    }

    private fun getAllBooks(){
        bookUseCases.getBooksByStatusUseCase()
            .onEach { books -> _totalPagesRead.value = books.sumOf { it.book.currentPage } }
            .launchIn(viewModelScope)
    }
}