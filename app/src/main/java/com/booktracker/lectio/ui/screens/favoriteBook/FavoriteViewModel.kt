package com.booktracker.lectio.ui.screens.favoriteBook

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.booktracker.lectio.domain.model.BookWithGenres
import com.booktracker.lectio.domain.usecase.BookUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import androidx.lifecycle.viewModelScope

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val bookUseCases: BookUseCases) : ViewModel() {
    private val _favoriteBooks = mutableStateOf<List<BookWithGenres>>(emptyList())
    val favoriteBooks: State<List<BookWithGenres>> = _favoriteBooks

    init {
        fetchFavoriteBooks()
    }

    private fun fetchFavoriteBooks() {
        bookUseCases.getFavoriteBookUseCase().onEach { books ->
            _favoriteBooks.value = books
        }.launchIn(viewModelScope)
    }







}