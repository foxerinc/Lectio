package com.booktracker.lectio.ui.screens.main

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.booktracker.lectio.ui.navigation.BottomNavigationBar
import com.booktracker.lectio.ui.navigation.Screen
import androidx.compose.ui.platform.LocalContext
import com.booktracker.lectio.ui.navigation.AppNavGraph

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

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                items = screens
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        AppNavGraph(
            navController = navController,
            innerPadding = innerPadding
        )
    }
}

