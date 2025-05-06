package com.booktracker.lectio.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.booktracker.lectio.domain.model.Genre
import com.booktracker.lectio.utils.BookStatusType
import java.util.Locale

@Composable
fun BookFormFields(
    title: String,
    onTitleChange: (String) -> Unit,
    titleError: String? = null,
    author: String,
    onAuthorChange: (String) -> Unit,
    authorError: String? = null,
    description: String,
    onDescriptionChange: (String) -> Unit,
    genres: List<Genre>,
    selectedGenre: List<Genre>,
    onGenreSelected: (List<Genre>) -> Unit,
    onAddNewGenre: (String) -> Unit,
    genresError: String? = null,
    status: BookStatusType,
    onStatusChange: (BookStatusType) -> Unit,
    currentPage: String,
    onCurrentPageChange: (String) -> Unit,
    currentPageError: String? = null,
    totalPage: String,
    onTotalPageChange: (String) -> Unit,
    totalPageError: String? = null,
    notes: String,
    onNotesChange: (String) -> Unit,
    notesError: String? = null,
    personalRating: Float,
    onPersonalRatingChange: (Float) -> Unit,
    personalRatingError: String? = null
) {
    Column (
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        //book tittle
        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Book Title") },
            isError = titleError != null,
            supportingText = titleError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            )

        )

        //book author
        OutlinedTextField(
            value = author,
            onValueChange = onAuthorChange,
            label = { Text("Book Author") },
            isError = authorError != null,
            supportingText = authorError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        //book description
        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Book Description") },
            modifier = Modifier.fillMaxWidth().height(100.dp),
            maxLines = 4,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        // genre : multiple selection from database or input new genre that have not yet been input can input more than once
        Text(
            text = "Book Genres",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        var newGenreName by remember { mutableStateOf("") }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(genres) { genre ->
                val isSelected = selectedGenre.contains(genre)
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        onGenreSelected(
                            if (isSelected){
                                selectedGenre - genre
                            }else{
                                selectedGenre + genre
                            }
                        )
                    },
                    label = { Text(genre.name) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.surface,
                        labelColor = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.animateContentSize()
                )

            }

        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            OutlinedTextField(
                value = newGenreName,
                onValueChange = { newGenreName = it },
                label = { Text("Add New Genre") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            Button(
                onClick = {
                    if (newGenreName.isNotBlank()) {
                        onAddNewGenre(newGenreName)
                        val newGenre = Genre(id = 0, name = newGenreName)
                        onGenreSelected(selectedGenre + newGenre)
                        newGenreName = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Add Genre")
            }
        }

        // Display genres error if present
        genresError?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }


        //book status (want to read, reading, finished)
        Text(
            text = "Book Status",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            BookStatusType.entries.forEach { statusOption ->
                val isSelected = status == statusOption
                Button(
                    onClick = { onStatusChange(statusOption) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = statusOption.name.lowercase().replace("_", " ")
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }


        // current page : if want to read form current page disable so current page = 0, if reading form current page enable, if finished form current page disable take value from total page so current page = total book page
        AnimatedVisibility(
            visible = status != BookStatusType.WANT_TO_READ,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            OutlinedTextField(
                value = if (status == BookStatusType.FINISHED_READING) totalPage else currentPage,
                onValueChange = {
                    if (status != BookStatusType.FINISHED_READING) {
                        onCurrentPageChange(it.filter { char -> char.isDigit() })
                    }
                },
                label = { Text("Current Page") },
                isError = currentPageError != null,
                supportingText = currentPageError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth(),
                enabled = status != BookStatusType.FINISHED_READING,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    disabledTextColor = MaterialTheme.colorScheme.onBackground,
                    disabledBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    disabledLabelColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            )
        }

        //total book page
        OutlinedTextField(
            value = totalPage,
            onValueChange = { onTotalPageChange(it.filter { char -> char.isDigit() }) },
            label = { Text("Total Book Pages") },
            isError = totalPageError != null,
            supportingText = totalPageError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        //book notes
        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            label = { Text("Notes") },
            isError = notesError != null,
            supportingText = notesError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            modifier = Modifier.fillMaxWidth().height(100.dp),
            maxLines = 4,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )

        //personal rating : enabled only if finished
        AnimatedVisibility(
            visible = status == BookStatusType.FINISHED_READING,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Column {
                Text(
                    text = "Personal Rating",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (personalRating >= i) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { onPersonalRatingChange(i.toFloat()) }
                        )
                    }
                }
                personalRatingError?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

    }
    
}