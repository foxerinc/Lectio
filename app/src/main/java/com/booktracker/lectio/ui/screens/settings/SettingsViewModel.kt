package com.booktracker.lectio.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.booktracker.lectio.domain.usecase.BookUseCases
import com.booktracker.lectio.utils.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _notificationsEnabled = MutableStateFlow(true) // Added
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow() // Added

    private val _clearDataResult = MutableStateFlow<String?>(null)
    val clearDataResult: StateFlow<String?> = _clearDataResult.asStateFlow()

    init {
        // Load initial theme preference
        viewModelScope.launch {
            preferencesManager.themePreference.collect { theme ->
                _themePreference.value = theme
            }
        }
        // Load initial notification preference
        viewModelScope.launch {
            preferencesManager.notificationsEnabled.collect { enabled ->
                _notificationsEnabled.value = enabled
            }
        }
    }

    fun setThemePreference(theme: PreferencesManager.ThemePreference) {
        viewModelScope.launch {
            preferencesManager.setThemePreference(theme)
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setNotificationsEnabled(enabled)
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