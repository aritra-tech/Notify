package com.aritradas.notify.ui.screens.settingsScreen

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aritradas.notify.components.biometric.AppBioMetricManager
import com.aritradas.notify.components.biometric.BiometricAuthListener
import com.aritradas.notify.data.db.NoteDatabase
import com.aritradas.notify.domain.models.Note
import com.aritradas.notify.domain.repository.BackupRepository
import com.aritradas.notify.domain.repository.NoteRepository
import com.aritradas.notify.utils.DataStoreUtil
import com.aritradas.notify.ui.screens.MainActivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val settingsRepository: NoteRepository,
    private val appBioMetricManager: AppBioMetricManager,
    dataStoreUtil: DataStoreUtil,
) : AndroidViewModel(application) {

    private val backupRepository = BackupRepository(
        // Pass the appropriate NoteDatabase instance
        provider = NoteDatabase.getInstance(application),
        // Pass the application context
        context = application,
        // Create a new Mutex instance
        mutex = Mutex(),
        // Pass the viewModelScope
        scope = viewModelScope,
        // Pass the appropriate coroutine dispatcher
        dispatcher = Dispatchers.IO
    )
    var notes by mutableStateOf(emptyList<Note>())

    private var observeNoteJob: Job? = null

    private val dataStore = dataStoreUtil.dataStore
    private val _biometricAuthState = MutableStateFlow(false)
    val biometricAuthState: StateFlow<Boolean> = _biometricAuthState

    init {
        observe()
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.data.map { preferences ->
                preferences[DataStoreUtil.IS_BIOMETRIC_AUTH_SET_KEY] ?: false
            }.collect {
                _biometricAuthState.value = it
            }
        }
    }

    fun showBiometricPrompt(activity: MainActivity) {
        appBioMetricManager.initBiometricPrompt(
            activity = activity,
            listener = object : BiometricAuthListener {
                override fun onBiometricAuthSuccess() {
                    viewModelScope.launch {
                        dataStore.edit { preferences ->
                            preferences[DataStoreUtil.IS_BIOMETRIC_AUTH_SET_KEY] =
                                !_biometricAuthState.value
                        }
                    }
                }

                override fun onUserCancelled() {
                }

                override fun onErrorOccurred() {
                }
            }
        )
    }

    fun onExport(uri: Uri) {
        viewModelScope.launch {
            backupRepository.export(uri)
            observe()
        }
    }

    fun onImport(uri: Uri) {
        viewModelScope.launch {
            backupRepository.import(uri)
            observe()
        }
    }

    private fun observe() {
        observeNotes()
    }

    private fun observeNotes() {
        observeNoteJob?.cancel()
        observeNoteJob = viewModelScope.launch {
            settingsRepository.getAllNotesFromRoom().collect { note ->
                notes = note
            }
        }
    }
}
