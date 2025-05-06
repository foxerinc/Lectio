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
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.booktracker.lectio.ui.screens.splash.SplashScreen
import com.booktracker.lectio.utils.NotificationScheduler
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestNotificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
        } else {
            // Permission denied; notifications won't work
            // The switch toggle logic in SettingsScreen will handle further requests
            Toast.makeText(this, "Notifications permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val themePreference by settingsViewModel.themePreference.collectAsState()
            val notificationsEnabled by settingsViewModel.notificationsEnabled.collectAsState()

            if (notificationsEnabled) {
                NotificationScheduler.scheduleReadingReminder(this)
            }

            val darkTheme = when (themePreference) {
                PreferencesManager.ThemePreference.LIGHT -> false
                PreferencesManager.ThemePreference.DARK -> true
                PreferencesManager.ThemePreference.SYSTEM -> null
            }

            LectioTheme(
                darkTheme = darkTheme ?: androidx.compose.foundation.isSystemInDarkTheme(),
                dynamicColor = false
            ) {
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

        // Request notification permission for Android 13+ on app launch
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == android.content.pm.PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


}

