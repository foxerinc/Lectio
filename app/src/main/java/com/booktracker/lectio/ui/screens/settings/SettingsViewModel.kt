package com.booktracker.lectio.ui.screens.settings

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booktracker.lectio.domain.usecase.BookUseCases
import com.booktracker.lectio.utils.NotificationScheduler
import com.booktracker.lectio.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val bookUseCases: BookUseCases
): ViewModel() {

    private val _themePreference = MutableStateFlow(PreferencesManager.ThemePreference.SYSTEM)
    val themePreference: StateFlow<PreferencesManager.ThemePreference> = _themePreference.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(false)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _clearDataResult = MutableStateFlow<String?>(null)
    val clearDataResult: StateFlow<String?> = _clearDataResult.asStateFlow()

    private val _requestPermission = MutableStateFlow(true)
    val requestPermission: StateFlow<Boolean> = _requestPermission.asStateFlow()

    init {
        // Load initial theme preference
        viewModelScope.launch(Dispatchers.IO){
            launch {
                preferencesManager.themePreference.collect { theme ->
                    Log.d("SettingsViewModel", "Loaded theme preference: $theme")
                    _themePreference.value = theme
                }
            }

            launch {
                preferencesManager.notificationsEnabled.collect { enabled ->
                    Log.d("SettingsViewModel", "Loaded notifications enabled: $enabled")
                    _notificationsEnabled.value = enabled
                }
            }

            launch {
                preferencesManager.notificationPermissionRequested.collect { requested ->
                    Log.d("SettingsViewModel", "Loaded notification permission requested: $requested")
                    _requestPermission.value = !requested && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                }
            }
        }
    }

    fun setThemePreference(theme: PreferencesManager.ThemePreference) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                preferencesManager.setThemePreference(theme)
                _themePreference.value = theme
                Log.d("SettingsViewModel", "Set themePreference to: $theme")
            }
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                preferencesManager.setNotificationsEnabled(enabled)
                _notificationsEnabled.value = enabled
                Log.d("SettingsViewModel", "Set notificationsEnabled to: $enabled")
            }

        }
    }

    fun onPermissionRequested() {
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                preferencesManager.setNotificationPermissionRequested(true)
                _requestPermission.value = false
                Log.d("SettingsViewModel", "Set notificationPermissionRequested to true")
            }
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO){
                    bookUseCases.deleteAllBookUseCase()
                }
                _clearDataResult.value = "All data cleared successfully"
            } catch (e: Exception) {
                _clearDataResult.value = "Failed to clear data: ${e.message}"
            }
        }
    }

    fun clearResultMessage() {
        _clearDataResult.value = null
    }
}