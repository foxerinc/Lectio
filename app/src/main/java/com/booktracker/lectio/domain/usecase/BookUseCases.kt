package com.booktracker.lectio.domain.usecase

data class BookUseCases(
    val getBooksByStatusUseCase: GetBooksByStatusUseCase,
    val addBookWithGenreUseCase: AddBookWithGenreUseCase,
    val getAllBookUseCase: GetAllBookUseCase,
    val getFavoriteBookUseCase: GetFavoriteBookUseCase,
    val getBookWithGenresByIdUseCase: GetBookWithGenresByIdUseCase,
    val updateBookUseCase: UpdateBookWithGenreUseCase,
    val deleteBookUseCase: DeleteBookUseCase,
    val deleteAllBookUseCase: DeleteAllBookUseCase,
    val updateBookFavoriteStatusUseCase: UpdateBookFavoriteStatusUseCase,

)

