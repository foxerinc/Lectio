package com.booktracker.lectio.data.mapper
import com.booktracker.lectio.data.datasource.local.entity.BookEntity
import com.booktracker.lectio.domain.model.Book

fun BookEntity.toDomain(): Book {
    return Book(
        id = this.bookId,
        title = this.title,
        author = this.author,
        description = this.description,
        coverImageUri = this.coverImageUri,
        totalPage = this.totalPage,
        currentPage = this.currentPage,
        isFavorite = this.isFavorite,
        status = this.status
    )
}

fun Book.toEntity(): BookEntity {
    return BookEntity(
        bookId = this.id,
        title = this.title,
        author = this.author ?: "",
        description = this.description,
        coverImageUri = this.coverImageUri,
        totalPage = this.totalPage,
        currentPage = this.currentPage,
        isFavorite = this.isFavorite,
        status = this.status,
        bookAddedInMillis = System.currentTimeMillis() // use current timestamp on insert
    )
}