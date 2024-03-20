package com.aritra.notify.viewmodel

import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aritra.notify.utils.DataStoreUtil
import com.aritra.notify.utils.DataStoreUtil.Companion.IS_DARK_MODE_KEY
import com.aritra.notify.utils.DataStoreUtil.Companion.IS_SECURE_ENV_KEY
import com.aritra.notify.domain.models.ThemeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(dataStoreUtil: DataStoreUtil) : ViewModel() {

    private val _themeState = MutableStateFlow(ThemeState(isDarkMode = false, isSecureEnv = false))
    val themeState: StateFlow<ThemeState> = _themeState

    private val dataStore = dataStoreUtil.dataStore

    init {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.data.map { preferences ->
                ThemeState(
                    isDarkMode = preferences[IS_DARK_MODE_KEY] ?: false,
                    isSecureEnv = preferences[IS_SECURE_ENV_KEY] ?: false
                )
            }.collect {
                _themeState.value = it
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[IS_DARK_MODE_KEY] = !(preferences[IS_DARK_MODE_KEY] ?: false)
            }
        }
    }

    fun toggleSecureEnv() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.edit { preferences ->
                preferences[IS_SECURE_ENV_KEY] = !(preferences[IS_SECURE_ENV_KEY] ?: false)
            }
        }
    }
}
