package com.booktracker.lectio.ui.screens.detailBook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booktracker.lectio.domain.model.BookWithGenres
import com.booktracker.lectio.domain.usecase.BookUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailBookViewModel @Inject constructor(private val bookUseCases: BookUseCases) : ViewModel() {
    private val _bookDetail = MutableStateFlow<BookWithGenres?>(null)
    val bookDetail: StateFlow<BookWithGenres?> = _bookDetail

    private val _deleteBookResult = MutableStateFlow<String?>(null)
    val deleteBookResult: StateFlow<String?> = _deleteBookResult



    fun loadBook(bookId: Int){
        viewModelScope.launch {
            bookUseCases.getBookWithGenresByIdUseCase(bookId).collect{

                _bookDetail.value = it
            }
        }
    }

    fun deleteBook(book: BookWithGenres){
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    bookUseCases.deleteBookUseCase(book.book)
                }
                _bookDetail.value = null
                _deleteBookResult.value = "Book deleted successfully"

            }catch (e: Exception){
                _deleteBookResult.value = "Failed to delete book: ${e.message}"
            }

        }
    }

    fun toggleFavoriteStatus(bookId: Int, favorite: Boolean){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                bookUseCases.updateBookFavoriteStatusUseCase(bookId, favorite)
            }
            loadBook(bookId)
        }

    }

    fun clearDeleteBookResult() {
        _deleteBookResult.value = null
    }
}