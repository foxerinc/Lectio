package com.booktracker.lectio.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme_preference")
        private val NOTIFICATION_KEY = booleanPreferencesKey("notifications_enabled")
    }

    // Available theme options
    enum class ThemePreference {
        LIGHT, DARK, SYSTEM
    }

    // Save theme preference
    suspend fun setThemePreference(theme: ThemePreference) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }

    // Get theme preference
    val themePreference: Flow<ThemePreference> = context.dataStore.data.map { preferences ->
        val themeName = preferences[THEME_KEY] ?: ThemePreference.SYSTEM.name
        ThemePreference.valueOf(themeName)
    }

    // Save notification preference
    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_KEY] = enabled
        }
    }

    // Get notification preference
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATION_KEY] ?: true // Default to true
    }
}