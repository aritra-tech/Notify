package com.aritra.notify.navigation

sealed class NotifyScreens(val name: String) {

    object Notes : NotifyScreens("notes")
    object AddEditNotes : NotifyScreens("add_edit_note")
    object TodoHome : NotifyScreens("todo_home")
    object Settings : NotifyScreens("setting")
    object TrashNoteScreen : NotifyScreens("trash_note_route")
}
