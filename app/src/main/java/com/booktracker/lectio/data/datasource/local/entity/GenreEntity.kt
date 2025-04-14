package com.booktracker.lectio.data.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "genres")
data class GenreEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "genre_id")
    val genreId: Int = 0,
    @ColumnInfo(name = "name")
    val name: String
)

@Entity(
    tableName = "book_genre_cross_ref",
    primaryKeys = ["book_id", "genre_id"],
    foreignKeys = [
        ForeignKey(
            entity = BookEntity::class,
            parentColumns = ["book_id"],
            childColumns = ["bookId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = GenreEntity::class,
            parentColumns = ["genre_id"],
            childColumns = ["genreId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("bookId"), Index("genreId")]
)
data class BookGenreCrossRef(
    val bookId: Int,
    val genreId: Int
)

data class BookWithGenresEntity(
    @Embedded val bookEntity: BookEntity,
    @Relation(
        parentColumn = "book_id",
        entityColumn = "genre_id",
        associateBy = Junction(BookGenreCrossRef::class)
    )
    val genreEntities: List<GenreEntity>
)