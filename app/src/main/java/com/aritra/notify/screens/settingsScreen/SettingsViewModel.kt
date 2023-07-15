package com.aritra.notify.screens.settingsScreen

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aritra.notify.data.db.NoteDatabase
import com.aritra.notify.data.models.Note
import com.aritra.notify.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    application: Application,
    private val settingsRepository: NoteRepository
) : AndroidViewModel(application) {

    private val backupRepository = BackupRepository(
        provider = NoteDatabase.getInstance(application), // Pass the appropriate NoteDatabase instance
        context = application, // Pass the application context
        mutex = Mutex(), // Create a new Mutex instance
        scope = viewModelScope, // Pass the viewModelScope
        dispatcher = Dispatchers.IO // Pass the appropriate coroutine dispatcher
    )
    var notes by mutableStateOf(emptyList<Note>())

    private var observeNoteJob : Job? = null

    init {
        observe()
    }

    fun onExport(uri: Uri) {
        viewModelScope.launch {
            backupRepository.export(uri)
            observe()
        }
    }

    fun onImport(uri: Uri){
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

    fun openNotify(context: Context){
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/aritra-tech/Notify"))
        context.startActivity(intent)
    }
}