package com.booktracker.lectio.ui.screens.settings

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.booktracker.lectio.utils.NotificationScheduler
import com.booktracker.lectio.utils.PreferencesManager
import kotlinx.coroutines.launch
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
) {
    val notificationsEnabled by viewModel.notificationsEnabled.collectAsState()
    val themePreference by viewModel.themePreference.collectAsState()
    val clearDataResult by viewModel.clearDataResult.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity
    val scope = rememberCoroutineScope()

    // State for Clear All Data confirmation dialog
    var showClearDataDialog by remember { mutableStateOf(false) }
    var showClearMessage by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }


    val snackbarHostState = remember { SnackbarHostState() }



    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        scope.launch {
            if (isGranted) {
                snackbarHostState.showSnackbar("Notifications permission granted")
                NotificationScheduler.scheduleReadingReminder(context)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    val shouldShowRationale = activity?.let { ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.POST_NOTIFICATIONS) }
                    if (shouldShowRationale == false){
                        showSettingsDialog = true
                    }else{
                        snackbarHostState.showSnackbar("Notifications permission denied")
                    }
                }
            }
        }
        viewModel.setNotificationsEnabled(isGranted)
    }





    LaunchedEffect(showClearMessage) {
        if (showClearMessage){
            clearDataResult?.let { snackbarHostState.showSnackbar(it) }
            viewModel.clearResultMessage()
            showClearMessage = false
        }
    }

    if(showClearDataDialog){
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = { Text("Clear All Data") },
            text = { Text("Are you sure you want to clear all data? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllData()
                        showClearMessage = true
                        showClearDataDialog = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Permission Required") },
            text = { Text("Notifications are disabled. Please enable them in app settings.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showSettingsDialog = false
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        }
                        context.startActivity(intent)
                    }
                ) {
                    Text("Go to Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSettingsDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.padding(16.dp)
            )}
    ) { innerPadding ->
        //notification settings
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
        ){
            // Notifications Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications Icon",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Enable Notifications",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Switch(
                    checked = notificationsEnabled,
                    onCheckedChange = { isChecked ->
                        if (isChecked){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                                val permission = Manifest.permission.POST_NOTIFICATIONS
                                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                                    viewModel.setNotificationsEnabled(true)
                                    NotificationScheduler.scheduleReadingReminder(context)
                                } else {
                                    requestPermissionLauncher.launch(permission)
                                }
                            }else{
                                viewModel.setNotificationsEnabled(true)
                                NotificationScheduler.scheduleReadingReminder(context)
                            }
                        }else{
                            viewModel.setNotificationsEnabled(false)
                            NotificationScheduler.cancelReadingReminder(context)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            //theme settings
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.DarkMode,
                        contentDescription = "Theme Icon",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Theme",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            val themes = listOf(
                PreferencesManager.ThemePreference.LIGHT to "Light",
                PreferencesManager.ThemePreference.DARK to "Dark",
                PreferencesManager.ThemePreference.SYSTEM to "System Default"
            )
            themes.forEach { (theme, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp, top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )
                    RadioButton(
                        selected = themePreference == theme,
                        onClick = { viewModel.setThemePreference(theme) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Clear All Data
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Data Icon",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Clear All Data",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Button(
                    onClick = { showClearDataDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Clear")
                }
            }

        }

    }

}