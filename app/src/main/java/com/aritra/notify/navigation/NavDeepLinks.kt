package com.aritra.notify.navigation

import androidx.core.net.toUri

object NavDeepLinks {

    private const val BASE_URI = "app://com.aritra.notify"

    // deeplinks for add notes screen set to -1 means only to add note
    val addNotesUriPattern = BASE_URI + NotifyScreens.AddEditNotes.name + "/{noteId}"
    val addNotesUri = (BASE_URI + NotifyScreens.AddEditNotes.name + "/-1").toUri()

    // deeplinks to open trash notes
    val trashNoteUriPattern = BASE_URI + NotifyScreens.TrashNoteScreen.name
    val trashNoteUri = trashNoteUriPattern.toUri()
}
