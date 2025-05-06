package com.booktracker.lectio.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    data object Dashboard : Screen("dashboard", Icons.Default.Home, "Dashboard")
    data object Library : Screen("library", Icons.Default.Book, "Library")
    data object AddBook : Screen("add_book", Icons.Default.Add, "Add Book")
    data object Favorite : Screen("favorite", Icons.Default.Favorite, "Favorite")
    data object Settings : Screen("settings", Icons.Default.Settings, "Settings")
}