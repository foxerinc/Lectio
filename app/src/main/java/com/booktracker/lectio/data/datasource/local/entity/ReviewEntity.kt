package com.booktracker.lectio.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "reviews",
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["book_id"],
            childColumns = ["book_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("bookId")]
)
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "review_id")
    val review_id: Int = 0,
    @ColumnInfo(name = "book_id")
    val bookId: Int,
    @ColumnInfo(name = "reviewer_name")
    val reviewerName: String,
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "rating")
    val rating: Float,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)