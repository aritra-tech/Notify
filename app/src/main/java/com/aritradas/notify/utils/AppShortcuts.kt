package com.aritradas.notify.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.aritradas.notify.R
import com.aritradas.notify.navigation.NavDeepLinks
import com.aritradas.notify.ui.screens.MainActivity

object AppShortcuts {

    private fun createNoteShortCut(context: Context): ShortcutInfoCompat =
        ShortcutInfoCompat.Builder(context, "add_new_note")
            .setShortLabel(context.getString(R.string.add_new_note_shortcut_label))
            .setIcon(IconCompat.createWithResource(context, R.drawable.shortcut_add_icon))
            .setIntent(
                Intent(context, MainActivity::class.java).apply {
                    // opens create note screen
                    data = NavDeepLinks.addNotesUri
                    action = Intent.ACTION_VIEW
                }
            )
            .build()

    fun showShortCuts(context: Context) {
        val shortcut1 = createNoteShortCut(context)
        try {
            val isSuccess = ShortcutManagerCompat.addDynamicShortcuts(context, listOf(shortcut1))
            Log.d("SHORTCUTS", "CREATED $isSuccess")
        } catch (e: Exception) {
            Log.e("SHORTCUTS", "ERROR IN LAYING SHORTCUTS", e)
        }
    }
}
