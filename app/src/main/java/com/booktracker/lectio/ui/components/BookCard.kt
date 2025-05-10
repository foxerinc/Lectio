package com.booktracker.lectio.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.booktracker.lectio.domain.model.BookWithGenres
import coil.compose.AsyncImage
import com.booktracker.lectio.R
import com.booktracker.lectio.domain.model.Book
import com.booktracker.lectio.domain.model.Genre
import com.booktracker.lectio.ui.theme.LectioTheme
import com.booktracker.lectio.utils.BookStatusType


@Composable
fun BookCard(
    book: BookWithGenres,
    onClick: (Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .clickable { onClick(book.book.id) }
            .fillMaxWidth()
            .heightIn(max = 200.dp)
            .padding(horizontal = 0.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp)
                ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Book Cover Image (Left/Start, height = 160.dp, width = 80.dp)
            AsyncImage(
                model = book.book.coverImageUri ?: R.drawable.baseline_image_24,
                contentDescription = book.book.title,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(100.dp)
                    .height(160.dp)
                    .fillMaxHeight()

            )

            Spacer(modifier = Modifier.width(8.dp))

            // Text Details (Title, Author, Genre - Middle, centered vertically)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = book.book.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2,
                )

                Spacer(modifier = Modifier.height(5.dp))

                if (!book.book.author.isNullOrBlank()) {
                    Text(
                        text = book.book.author,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    book.genres.take(5).forEach { genre ->
                        Text(
                            text = genre.name,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Progress Section (Right/End, centered vertically)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(start = 4.dp, end = 10.dp) // Reduced from 8.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(40.dp)
                ) {
                    val progress = if (book.book.totalPage == 0) 0f
                    else book.book.currentPage / book.book.totalPage.toFloat()
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxSize(),
                        strokeWidth = 4.dp,
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Page ${book.book.currentPage}/${book.book.totalPage}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookCardPreview() {
    val sampleBook = BookWithGenres(
        book = Book(
            id = 1,
            title = "Book Title Book Title Book Title Book Title",
            author = "Author 1",
            currentPage = 50,
            totalPage = 200,
            coverImageUri = null,
            status = BookStatusType.CURRENTLY_READING,
            description = "",
            isFavorite = false,
            personalRating = 4.5f,
            notes = "",
            bookAddedInMillis = 0L,
        ),
        genres = listOf(
            Genre(1, "Fiction"),
            Genre(2, "Adventure")
        )
    )

    LectioTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            BookCard(
                book = sampleBook,
                onClick = {},
            )
        }
    }
}


