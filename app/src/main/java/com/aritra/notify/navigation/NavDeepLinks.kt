package com.aritra.notify.navigation

import androidx.core.net.toUri

object NavDeepLinks {

    private const val baseUri = "app://com.aritra.notify"

    // deeplinks for add notes screen set to -1 means only to add note
    val addNotesUriPattern = baseUri + NotifyScreens.AddEditNotes.name + "/{noteId}"
    val addNotesUri = (baseUri + NotifyScreens.AddEditNotes.name + "/-1").toUri()

    // deeplinks to open trash notes
    val trashNoteUriPattern = baseUri + NotifyScreens.TrashNoteScreen.name
    val trashNoteUri = trashNoteUriPattern.toUri()

}