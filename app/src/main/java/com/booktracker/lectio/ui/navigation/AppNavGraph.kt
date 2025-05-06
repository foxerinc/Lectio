package com.booktracker.lectio.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import com.booktracker.lectio.ui.screens.addBook.AddBookScreen
import com.booktracker.lectio.ui.screens.addBook.AddBookViewModel
import com.booktracker.lectio.ui.screens.dashboard.DashboardScreen
import com.booktracker.lectio.ui.screens.dashboard.DashboardViewModel
import com.booktracker.lectio.ui.screens.detailBook.DetailBookScreen
import com.booktracker.lectio.ui.screens.detailBook.DetailBookViewModel
import com.booktracker.lectio.ui.screens.editBook.EditBookScreen
import com.booktracker.lectio.ui.screens.editBook.EditBookViewModel
import com.booktracker.lectio.ui.screens.favoriteBook.FavoriteScreen
import com.booktracker.lectio.ui.screens.favoriteBook.FavoriteViewModel
import com.booktracker.lectio.ui.screens.library.LibraryScreen
import com.booktracker.lectio.ui.screens.library.LibraryViewModel
import com.booktracker.lectio.ui.screens.settings.SettingsScreen
import com.booktracker.lectio.ui.screens.settings.SettingsViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier.padding(innerPadding)
    ) {

        composable(Screen.Dashboard.route) {
            val viewModel: DashboardViewModel = hiltViewModel()
            DashboardScreen(
                viewModel = viewModel,
                onBookClick = { bookId ->
                    navController.navigate("book_details/$bookId")
                }
            )
        }
        composable(Screen.Library.route) {
            val viewModel: LibraryViewModel = hiltViewModel()
            LibraryScreen(
                viewModel = viewModel,
                onBookClick = { bookId ->
                    navController.navigate("book_details/$bookId")
                }
            )
        }
        composable(Screen.AddBook.route) {
            val viewModel: AddBookViewModel = hiltViewModel()
            AddBookScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.Favorite.route) {
            val viewModel: FavoriteViewModel = hiltViewModel()
            FavoriteScreen(
                viewModel = viewModel,
                onBookClick = { bookId ->
                    navController.navigate("book_details/$bookId")
                }
            )
        }
        composable(Screen.Settings.route) {
            val viewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(viewModel = viewModel)
        }
        composable(
            route = "book_details/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId")
            val viewModel: DetailBookViewModel = hiltViewModel()
            bookId?.let {
                DetailBookScreen(
                    bookId = it,
                    viewModel = viewModel,
                    navController = navController
                )
            } ?: Text("Error: Book ID not found", modifier = Modifier.padding(16.dp))
        }
        composable(
            route = "edit_book/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId")
            val viewModel: EditBookViewModel = hiltViewModel()
            bookId?.let {
                EditBookScreen(
                    navController = navController,
                    bookId = it,
                    viewModel = viewModel
                )
            }
        }
    }
}