package com.aritradas.notify.navigation

import androidx.core.net.toUri

object NavDeepLinks {

    private const val BASE_URI = "app://com.aritra.notify"

    // deeplinks for add notes screen set to -1 means only to add note
    val addNotesUriPattern = BASE_URI + NotifyScreens.AddEditNotes.name + "/{noteId}" + "/{isPinned}"
    val addNotesUri = (BASE_URI + NotifyScreens.AddEditNotes.name + "/-1" + "/false").toUri()
}
