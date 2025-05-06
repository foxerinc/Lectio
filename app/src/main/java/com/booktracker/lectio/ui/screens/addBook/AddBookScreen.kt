package com.booktracker.lectio.ui.screens.addBook

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.booktracker.lectio.domain.model.Genre
import com.booktracker.lectio.ui.components.BookFormFields
import com.booktracker.lectio.ui.components.CancelConfirmationDialog
import com.booktracker.lectio.ui.components.ImagePicker
import com.booktracker.lectio.ui.components.SectionTitle
import com.booktracker.lectio.ui.navigation.Screen
import com.booktracker.lectio.utils.BookStatusType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(
    navController: NavController,
    viewModel: AddBookViewModel
) {
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var coverImageUri by remember { mutableStateOf<String?>(null) }
    var status by remember { mutableStateOf(BookStatusType.WANT_TO_READ) }
    var currentPage by remember { mutableStateOf("") }
    var totalPage by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var personalRating by remember { mutableFloatStateOf(0f) }
    var selectedGenres by remember { mutableStateOf<List<Genre>>(emptyList()) }

    val genres by viewModel.genres
    val isBookAdded by viewModel.isBookAdded
    val errorMessage by viewModel.errorMessage.collectAsState()

    var dismissSnackbar by remember { mutableStateOf(false) }

    // State for showing the cancel confirmation dialog
    var showCancelDialog by remember { mutableStateOf(false) }

    // Snackbar host state for showing errors
    val snackbarHostState = remember { SnackbarHostState() }

    // Navigate back to Dashboard after successful save
    LaunchedEffect(isBookAdded) {
        if (isBookAdded) {
            dismissSnackbar = true
            snackbarHostState.showSnackbar("Book added successfully", duration = SnackbarDuration.Short)
            viewModel.resetState()
            navController.navigateUp()

        }
    }

    LaunchedEffect(dismissSnackbar) {
        if (dismissSnackbar) {
            delay(800)
            snackbarHostState.currentSnackbarData?.dismiss()
            dismissSnackbar = false
        }

    }

    // Show error message in a Snackbar if present
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.resetState()
        }
    }

    // Show Cancel Dialog Form
    if (showCancelDialog){
        CancelConfirmationDialog(onDismiss = { showCancelDialog = false },
            onConfirm = {
                showCancelDialog = false
                navController.navigateUp()
            })
    }

    BackHandler {
        showCancelDialog = true
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Book") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionTitle(title = "Add a New Book")
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Image Picker
                    ImagePicker(
                        coverImageUri = coverImageUri,
                        onImagePicked = { coverImageUri = it }
                    )

                    // Form Fields
                    BookFormFields(
                        title = title,
                        onTitleChange = { title = it },
                        titleError = viewModel.titleError.value,
                        author = author,
                        onAuthorChange = { author = it },
                        authorError = viewModel.authorError.value,
                        description = description,
                        onDescriptionChange = { description = it },
                        genres = genres,
                        selectedGenre = selectedGenres,
                        onGenreSelected = { selectedGenres = it },
                        onAddNewGenre = { viewModel.addNewGenre(it) },
                        genresError = viewModel.genresError.value,
                        status = status,
                        onStatusChange = { status = it },
                        currentPage = currentPage,
                        onCurrentPageChange = { currentPage = it },
                        currentPageError = viewModel.currentPageError.value,
                        totalPage = totalPage,
                        onTotalPageChange = { totalPage = it },
                        totalPageError = viewModel.totalPagesError.value,
                        notes = notes,
                        onNotesChange = { notes = it },
                        notesError = viewModel.notesError.value,
                        personalRating = personalRating,
                        onPersonalRatingChange = { personalRating = it },
                        personalRatingError = viewModel.ratingError.value
                    )
                }
            }


            // Save and Cancel Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        showCancelDialog = true
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
                ) {
                    Text("Cancel")
                }
                Button(
                    onClick = {
                        val pages = totalPage.toIntOrNull() ?: 0
                        val current = if (status == BookStatusType.FINISHED_READING) pages else currentPage.toIntOrNull() ?: 0
                        viewModel.addBook(
                            title, author, description, coverImageUri ?: "", status,
                            current, pages, notes, personalRating, selectedGenres
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Save")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

    }

}