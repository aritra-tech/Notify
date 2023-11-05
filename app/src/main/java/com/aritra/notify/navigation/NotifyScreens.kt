package com.aritra.notify.navigation

sealed class NotifyScreens(val name: String) {

    data object Notes : NotifyScreens("notes")
    data object AddEditNotes : NotifyScreens("add_edit_note")
    data object TodoHome : NotifyScreens("todo_home")
    data object Settings : NotifyScreens("setting")
    data object TrashNoteScreen : NotifyScreens("trash_note_route")
}
