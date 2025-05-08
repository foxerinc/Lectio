package com.booktracker.lectio.ui.screens.detailBook

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.booktracker.lectio.R
import com.booktracker.lectio.ui.components.DetailItem
import com.booktracker.lectio.ui.navigation.Screen
import com.booktracker.lectio.utils.BookStatusType
import com.booktracker.lectio.utils.NotificationScheduler
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBookScreen(
    viewModel: DetailBookViewModel,
    bookId: Int,
    navController: NavController,

    ) {
    val bookWithGenres by viewModel.bookDetail.collectAsState()
    val deleteBookResult by viewModel.deleteBookResult.collectAsState()

    var showDeleteBookDialog by remember { mutableStateOf(false) }
    var showDeleteMessage by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId)
    }

    LaunchedEffect(showDeleteMessage) {
        if (showDeleteMessage){
            bookWithGenres?.let {
                viewModel.deleteBook(it)
            }
            viewModel.clearDeleteBookResult()
            navController.popBackStack()
        }
    }


    if(showDeleteBookDialog){
        AlertDialog(
            onDismissRequest = { showDeleteBookDialog = false },
            title = {bookWithGenres?.let {
                Text("Delete Book ${it.book.title}")
            } },
            text = { Text("Are you sure you want to DELETE this book? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteBookDialog = false
                        showDeleteMessage = true
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteBookDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        snackbarHost = {
            androidx.compose.material3.SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
            )}
    ) { innerPadding ->
        bookWithGenres?.let { bookData ->

            // States for controlling visibility of each element
            val visibilityStates = remember { List(4) { mutableStateOf(false) } }

            // Trigger staggered animations when book data is loaded
            LaunchedEffect(bookData) {
                visibilityStates.forEachIndexed { index, state ->
                    delay(200L * index) // 200ms delay between each element
                    state.value = true
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Cover Image
                AnimatedVisibility(
                    visible = visibilityStates[0].value,
                    enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                        initialOffsetX = { -50 },
                        animationSpec = tween(500)
                    )
                ){
                    Box(
                        modifier = Modifier
                            .size(width = 200.dp, height = 250.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .shadow(4.dp, RoundedCornerShape(12.dp))
                            .padding(bottom = 8.dp)
                    ){
                        AsyncImage(
                            model = bookData.book.coverImageUri ?: R.drawable.baseline_image_24,
                            contentDescription = "Book Cover",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                    }
                }


                // Rating and Favorite Row
                AnimatedVisibility(
                    visible = visibilityStates[1].value,
                    enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                        initialOffsetX = { -50 },
                        animationSpec = tween(500)
                    )
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Rating (visible only if status is Finished)
                        AnimatedVisibility(
                            visible = bookData.book.status == BookStatusType.FINISHED_READING,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = if (bookData.book.personalRating != null) "${bookData.book.personalRating}/5" else "N/A",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Favorite Icon (clickable)
                        IconButton(onClick = {
                            viewModel.toggleFavoriteStatus(bookData.book.id, !bookData.book.isFavorite)
                        }) {
                            Icon(
                                imageVector = if (bookData.book.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (bookData.book.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }


                //Details Section
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ){
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        // Title
                        AnimatedVisibility(
                            visible = visibilityStates[2].value,
                            enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                initialOffsetX = { -50 },
                                animationSpec = tween(500)
                            )
                        ){
                            Text(
                                text = bookData.book.title,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                ),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }


                        // Author
                        AnimatedVisibility(
                            visible = visibilityStates[2].value,
                            enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                initialOffsetX = { -50 },
                                animationSpec = tween(500)
                            )
                        ){
                            bookData.book.author?.let {
                                DetailItem(
                                    label = "Author",
                                    value = it
                                )
                            }
                        }


                        // Genres
                        if (bookData.genres.isNotEmpty()) {
                            AnimatedVisibility(
                                visible = visibilityStates[2].value,
                                enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                    initialOffsetX = { -50 },
                                    animationSpec = tween(500)
                                )
                            ) {
                                DetailItem(
                                    label = "Genres",
                                    value = bookData.genres.joinToString(", ") { it.name }
                                )
                            }
                        }

                        // Description
                        bookData.book.description?.let {
                            AnimatedVisibility(
                                visible = visibilityStates[2].value,
                                enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                    initialOffsetX = { -50 },
                                    animationSpec = tween(500)
                                )
                            ) {
                                DetailItem(
                                    label = "Description",
                                    value = bookData.book.description
                                )
                            }
                        }

                        // Status
                        AnimatedVisibility(
                            visible = visibilityStates[2].value,
                            enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                initialOffsetX = { -50 },
                                animationSpec = tween(500)
                            )
                        ){
                            DetailItem(
                                label = "Status",
                                value = when (bookData.book.status) {
                                    BookStatusType.WANT_TO_READ -> "Want to Read"
                                    BookStatusType.CURRENTLY_READING -> "Currently Reading"
                                    BookStatusType.FINISHED_READING -> "Finished"
                                }
                            )
                        }


                        // Current Page (visible only if status is Currently Reading)
                        if (bookData.book.status == BookStatusType.CURRENTLY_READING) {
                            AnimatedVisibility(
                                visible = visibilityStates[2].value,
                                enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                    initialOffsetX = { -50 },
                                    animationSpec = tween(500)
                                )
                            ) {
                                DetailItem(
                                    label = "Current Page",
                                    value = bookData.book.currentPage.toString()
                                )
                            }
                        }

                        // Total Pages
                        AnimatedVisibility(
                            visible = visibilityStates[2].value,
                            enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                initialOffsetX = { -50 },
                                animationSpec = tween(500)
                            )
                        ) {
                            DetailItem(
                                label = "Total Pages",
                                value = bookData.book.totalPage.toString()
                            )
                        }

                        // Notes
                        AnimatedVisibility(
                            visible = visibilityStates[2].value,
                            enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                initialOffsetX = { -50 },
                                animationSpec = tween(500)
                            )
                        ){
                            bookData.book.notes?.let {
                                DetailItem(
                                    label = "Notes",
                                    value = bookData.book.notes
                                )
                            }
                        }


                        // Date Added (DD/MM/YYYY)
                        AnimatedVisibility(
                            visible = visibilityStates[2].value,
                            enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                                initialOffsetX = { -50 },
                                animationSpec = tween(500)
                            )
                        ) {
                            DetailItem(
                                label = "Date Added",
                                value = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                    .format(Date(bookData.book.bookAddedInMillis))
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                //Action Buttons
                AnimatedVisibility(
                    visible = visibilityStates[3].value,
                    enter = fadeIn(animationSpec = tween(500)) + slideInHorizontally(
                        initialOffsetX = { -50 },
                        animationSpec = tween(500)
                    )
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                showDeleteBookDialog = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Delete", fontSize = 16.sp)
                        }

                        Button(
                            onClick = {
                                navController.navigate("edit_book/${bookData.book.id}")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Update", fontSize = 16.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}