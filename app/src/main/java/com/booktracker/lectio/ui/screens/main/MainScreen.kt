package com.booktracker.lectio.ui.screens.main

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.booktracker.lectio.ui.navigation.BottomNavigationBar
import com.booktracker.lectio.ui.navigation.Screen
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.booktracker.lectio.ui.navigation.AppNavGraph
import com.booktracker.lectio.ui.screens.settings.SettingsViewModel
import kotlinx.coroutines.launch

@Composable
fun MainScreen(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val screens = listOf(
        Screen.Dashboard,
        Screen.Library,
        Screen.AddBook,
        Screen.Favorite,
        Screen.Settings
    )

    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showCustomDialog by remember { mutableStateOf(false) }
    val requestPermission by settingsViewModel.requestPermission.collectAsState()

    LaunchedEffect(requestPermission) {
        if (requestPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            showCustomDialog = true
        }
    }

    if (showCustomDialog){
        AlertDialog(
            onDismissRequest = { showCustomDialog = false },
            title = { androidx.compose.material3.Text("Welcome to Lectio") },
            text = { androidx.compose.material3.Text("Would you like to enable notifications for reading reminders?") },
            confirmButton = {
                androidx.compose.material3.Button(
                    onClick = {
                        showCustomDialog = false
                        navController.navigate("settings") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                        scope.launch {
                            settingsViewModel.onPermissionRequested()
                        }
                    }
                ) {
                    androidx.compose.material3.Text("Agree")
                }
            },
            dismissButton = {
                androidx.compose.material3.Button(
                    onClick = {
                        showCustomDialog = false
                        scope.launch {
                            settingsViewModel.onPermissionRequested()
                            snackbarHostState.showSnackbar("Notifications will not be enabled.")
                        }
                    }
                ) {
                    androidx.compose.material3.Text("Deny")
                }
            }
        )
    }



    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = screens
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            innerPadding = innerPadding
        )
    }
}

