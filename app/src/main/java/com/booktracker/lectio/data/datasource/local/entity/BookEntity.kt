package com.booktracker.lectio.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.booktracker.lectio.utils.BookStatusType

@Entity(tableName = "book")
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "book_id")
    val bookId: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "coverImageUri")
    val coverImageUri: String?,
    @ColumnInfo(name = "bookAddedInMillis")
    val bookAddedInMillis: Long,
    @ColumnInfo(name = "status")
    var status: BookStatusType = BookStatusType.WANT_TO_READ,
    @ColumnInfo(name = "currentPage")
    var currentPage: Int,
    @ColumnInfo(name = "totalPage")
    val totalPage: Int,
    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false,
    @ColumnInfo(name = "personal_rating")
    val personalRating: Float? = null,
    @ColumnInfo(name = "notes")
    val notes: String? = null
)