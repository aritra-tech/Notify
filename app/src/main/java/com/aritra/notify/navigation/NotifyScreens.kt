package com.aritra.notify.navigation

sealed class NotifyScreens(val name: String) {

    object Notes : NotifyScreens("notes")
    object AddNotes : NotifyScreens("add_note")
    object UpdateNotes : NotifyScreens("update_note")
    object TodoHome : NotifyScreens("todo_home")
    object Settings : NotifyScreens("setting")
}
