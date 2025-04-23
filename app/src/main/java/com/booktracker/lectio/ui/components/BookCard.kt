package com.booktracker.lectio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
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
import com.booktracker.lectio.utilis.BookStatusType


@Composable
fun BookCard(
    book: BookWithGenres,
    onClick: () -> Unit,

) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
            .fillMaxWidth()
            .wrapContentHeight()
            .heightIn(max = 400.dp, min = 330.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = book.book.coverImageUri ?: R.drawable.baseline_image_24,
                contentDescription = book.book.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Book Title
            Text(
                text = book.book.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Genre Texts
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                book.genres.take(2).forEach {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Author (jika ada)
            if (!book.book.author.isNullOrBlank()) {
                Text(
                    text = book.book.author,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Progress
            val progress = if (book.book.totalPage == 0) 0f
            else book.book.currentPage / book.book.totalPage.toFloat()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(40.dp)
                ) {
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxSize(),
                        strokeWidth = 4.dp,
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Page ${book.book.currentPage}/${book.book.totalPage}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}



    @Preview(showBackground = true)
    @Composable
    fun BookCardListPreview() {
        val sampleBooks = List(4) { index ->
            BookWithGenres(
                book = Book(
                    id = index,
                    title = if (index == 1 )"Book Title ${index} Checking Long Tittle how to see it and how to not see it" else "Book Title ${index} ",
                    author = "Author $index",
                    currentPage = 50 + index * 10,
                    totalPage = 200,
                    coverImageUri = null,
                    status = BookStatusType.CURRENTLY_READING,
                    description = "",
                    isFavorite = false,
                    personalRating = 4.5f,
                    notes = ""
                ),
                genres = listOf(
                    Genre(1, "Fiction"),
                    Genre(2, "Adventure")
                )
            )
        }

        LectioTheme {
            Surface(color = MaterialTheme.colorScheme.background) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(700.dp)
                ) {
                    items(sampleBooks) { book ->
                        BookCard(
                            book = book,
                            onClick = {},
                        )
                    }
                }
            }
        }
    }


