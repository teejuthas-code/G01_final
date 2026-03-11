package com.example.heartnote

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

class ThemeDataStore(private val context: Context) {

    companion object {
        val THEME = stringPreferencesKey("theme")
        val FONT = stringPreferencesKey("font")
    }

    val themeFlow: Flow<String> =
        context.dataStore.data.map { it[THEME] ?: "Pink" }

    val fontFlow: Flow<String> =
        context.dataStore.data.map { it[FONT] ?: "Default" }

    suspend fun saveTheme(theme: String) {
        context.dataStore.edit {
            it[THEME] = theme
        }
    }

    suspend fun saveFont(font: String) {
        context.dataStore.edit {
            it[FONT] = font
        }
    }
}