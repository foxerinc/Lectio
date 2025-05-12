package com.booktracker.lectio.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.booktracker.lectio.domain.model.BookWithGenres
import com.booktracker.lectio.ui.components.BookCard
import com.booktracker.lectio.ui.components.SectionTitle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onBookClick: (Int) -> Unit
){
    val currentlyReadingBooks by viewModel.currentlyReadingBooks.collectAsState()
    val totalPagesRead by viewModel.totalPagesRead.collectAsState()
    val finishedBooksCount by viewModel.finishedBooksCount.collectAsState()
    val currentlyReadingBooksCount by viewModel.currentlyReadingBooksCount.collectAsState()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(
                    text = "Dashboard",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold)
                )},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )

            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Section: Reading Stats
            SectionTitle(title = "Reading Statistics")
            Spacer(modifier = Modifier.height(8.dp))

            DashboardHeader(
                totalPages = totalPagesRead,
                finished = finishedBooksCount,
                currentlyReading = currentlyReadingBooksCount
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 📚 Section: Currently Reading
            SectionTitle(title = "Currently Reading")
            Spacer(modifier = Modifier.height(8.dp))

            if (currentlyReadingBooks.isEmpty()) {
                Text(
                    text = "You haven’t started reading any books yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(currentlyReadingBooks) { book ->
                        BookCard(
                            book = book,
                            onClick = { onBookClick(book.book.id) }
                        )
                    }
                }
            }
        }
    }

}




