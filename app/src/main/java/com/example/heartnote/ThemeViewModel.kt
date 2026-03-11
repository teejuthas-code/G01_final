package com.example.heartnote

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStore = ThemeDataStore(application)


    val theme = dataStore.themeFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        "Pink"
    )

    val font = dataStore.fontFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        "Default"
    )

    fun setTheme(theme: String) {
        viewModelScope.launch {
            dataStore.saveTheme(theme)
        }
    }

    fun setFont(font: String) {
        viewModelScope.launch {
            dataStore.saveFont(font)
        }
    }
}