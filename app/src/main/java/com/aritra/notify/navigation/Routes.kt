package com.aritra.notify.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object Notes: Routes()

    @Serializable
    data class AddEditNotes(val noteId: Int): Routes()

    @Serializable
    data object TrashNoteScreen: Routes()

    @Serializable
    data object Settings: Routes()
}