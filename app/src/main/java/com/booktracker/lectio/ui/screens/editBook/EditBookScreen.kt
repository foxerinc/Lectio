package com.booktracker.lectio.ui.screens.editBook

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.booktracker.lectio.domain.model.Genre
import com.booktracker.lectio.ui.components.BookFormFields
import com.booktracker.lectio.ui.components.CancelConfirmationDialog
import com.booktracker.lectio.ui.components.ImagePicker
import com.booktracker.lectio.utils.BookStatusType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBookScreen(
    navController: NavController,
    bookId: Int,
    viewModel: EditBookViewModel,
) {
    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId)
    }

    val bookWithGenres by viewModel.bookWithGenres.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isBookUpdated by viewModel.isBookUpdated

    // State for showing the cancel confirmation dialog
    var showCancelDialog by remember { mutableStateOf(false) }

    // Snackbar host state for showing errors
    val snackbarHostState = remember { SnackbarHostState() }

    // Show error message in a Snackbar if present
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearErrorMessage()
        }
    }

    // Show success message in a Snackbar if book is updated
    LaunchedEffect(isBookUpdated) {
        if (isBookUpdated) {
            navController.navigateUp()
            snackbarHostState.showSnackbar("Book updated successfully")
        }
    }

    // Show confirmation dialog for cancel action
    if (showCancelDialog) {
        CancelConfirmationDialog(
            onDismiss = { showCancelDialog = false },
            onConfirm = {
                showCancelDialog = false
                navController.navigateUp()
            }
        )
    }

    BackHandler {
        showCancelDialog = true
    }



    // State for update form fields
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedGenres by remember { mutableStateOf(listOf<Genre>()) }
    var status by remember { mutableStateOf(BookStatusType.WANT_TO_READ) }
    var currentPage by remember { mutableStateOf("") }
    var totalPages by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var personalRating by remember { mutableFloatStateOf(0f) }
    var coverImageUri by remember { mutableStateOf<String?>(null) }

    // Pre-fill form fields when bookWithGenres is loaded
    LaunchedEffect(bookWithGenres) {
        bookWithGenres?.let { bookWithGenresData ->
            title = bookWithGenresData.book.title
            author = bookWithGenresData.book.author ?: ""
            description = bookWithGenresData.book.description ?: ""
            selectedGenres = bookWithGenresData.genres.map { Genre(id = it.id, name = it.name) }
            status = bookWithGenresData.book.status
            currentPage = bookWithGenresData.book.currentPage.toString()
            totalPages = bookWithGenresData.book.totalPage.toString()
            notes = bookWithGenresData.book.notes ?: ""
            personalRating = bookWithGenresData.book.personalRating ?: 0f
            coverImageUri = bookWithGenresData.book.coverImageUri
        }
    }

    //form update
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Book") },
                navigationIcon = {
                    IconButton(onClick = { showCancelDialog = true }) {
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
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
            )
        }
    ){ innerPadding ->
        bookWithGenres?.let{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
            ){

                ImagePicker(
                    coverImageUri = coverImageUri,
                    onImagePicked = { coverImageUri = it }
                )

                BookFormFields(
                    title = title,
                    onTitleChange = { title = it },
                    titleError = viewModel.titleError.value,
                    author = author,
                    onAuthorChange = { author = it },
                    authorError = viewModel.authorError.value,
                    description = description,
                    onDescriptionChange = { description = it },
                    genres = selectedGenres,
                    selectedGenre = selectedGenres,
                    onGenreSelected = { selectedGenres = it },
                    onAddNewGenre = { newGenre ->
                        if (newGenre.isNotBlank() && selectedGenres.none { it.name == newGenre }) {
                            selectedGenres = selectedGenres + Genre(id = 0, name = newGenre)
                        }
                    },
                    genresError = viewModel.genresError.value,
                    status = status,
                    onStatusChange = { status = it },
                    currentPage = currentPage,
                    onCurrentPageChange = { currentPage = it },
                    currentPageError = viewModel.currentPageError.value,
                    totalPage = totalPages,
                    onTotalPageChange = { totalPages = it },
                    totalPageError = viewModel.totalPagesError.value,
                    notes = notes,
                    onNotesChange = { notes = it },
                    notesError = viewModel.notesError.value,
                    personalRating = personalRating,
                    onPersonalRatingChange = { personalRating = it },
                    personalRatingError = viewModel.ratingError.value
                )

                // Update and Cancel Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            showCancelDialog = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel", fontSize = 16.sp)
                    }

                    Button(
                        onClick = {
                            viewModel.updateBook(
                                bookId = bookId,
                                title = title,
                                author = author,
                                description = description,
                                coverImageUri = coverImageUri ?: "",
                                status = status,
                                currentPage = currentPage.toIntOrNull() ?: 0,
                                totalPage = totalPages.toIntOrNull() ?: 0,
                                notes = notes,
                                personalRating = personalRating,
                                selectedGenre = selectedGenres,
                                isFavorite = bookWithGenres?.book?.isFavorite ?: false
                            )
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
                Spacer(modifier = Modifier.height(16.dp))
            }
        } ?: run {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}