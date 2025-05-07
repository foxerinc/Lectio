package com.booktracker.lectio

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.booktracker.lectio.ui.screens.main.MainScreen
import com.booktracker.lectio.ui.screens.settings.SettingsViewModel
import com.booktracker.lectio.ui.theme.LectioTheme
import com.booktracker.lectio.utils.PreferencesManager
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.booktracker.lectio.ui.screens.splash.SplashScreen
import com.booktracker.lectio.utils.NotificationScheduler
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val themePreference by settingsViewModel.themePreference.collectAsState()

            val darkTheme = when (themePreference) {
                PreferencesManager.ThemePreference.LIGHT -> false
                PreferencesManager.ThemePreference.DARK -> true
                PreferencesManager.ThemePreference.SYSTEM -> null
            }

            LectioTheme(
                darkTheme = darkTheme ?: androidx.compose.foundation.isSystemInDarkTheme(),
                dynamicColor = false
            ) {
                Log.d("MainActivity", "onCreate: $darkTheme")
                var showSplash by remember { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    delay(5700)
                    showSplash = false
                }
                if (showSplash) {
                    SplashScreen()
                } else {
                    MainScreen()
                }

            }
        }

    }


}

