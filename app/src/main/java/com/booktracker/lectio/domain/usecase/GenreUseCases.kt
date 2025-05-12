package com.booktracker.lectio.domain.usecase

data class GenreUseCases (
    val getGenresUseCase: GetAllGenresUseCase,
    val insertGenresUseCase: InsertGenresUseCase,
    val deleteAllGenresUseCase: DeleteAllGenresUseCase



)