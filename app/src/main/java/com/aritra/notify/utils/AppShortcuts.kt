package com.aritra.notify.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.aritra.notify.R
import com.aritra.notify.navigation.NavDeepLinks
import com.aritra.notify.ui.screens.MainActivity

object AppShortcuts {

    private fun createNoteShortCut(context: Context): ShortcutInfoCompat {
        return ShortcutInfoCompat
            .Builder(context, "add_new_note")
            .setShortLabel(context.getString(R.string.add_new_note_shortcut_label))
            .setIcon(IconCompat.createWithResource(context, R.drawable.shortcut_add_icon))
            .setIntent(
                Intent(context, MainActivity::class.java).apply {
                    // opens create screen
                    data = NavDeepLinks.addNotesUri
                    action = Intent.ACTION_VIEW
                }
            )
            .build()
    }

    private fun openTrashShortCut(context: Context): ShortcutInfoCompat {
        return ShortcutInfoCompat
            .Builder(context, "open_trash")
            .setShortLabel(context.getString(R.string.open_trash_shortcut_label))
            .setIcon(IconCompat.createWithResource(context, R.drawable.shortcut_delete_icon))
            .setIntent(
                Intent(context, MainActivity::class.java).apply {
                    // opens create screen
                    data = NavDeepLinks.trashNoteUri
                    action = Intent.ACTION_VIEW
                }
            )
            .build()
    }

    fun showShortCuts(context: Context) {
        // shortcuts to appear
        val shortcut1 = createNoteShortCut(context)
        val shortcut2 = openTrashShortCut(context)
        try {

            val isSuccess = ShortcutManagerCompat.addDynamicShortcuts(context, listOf(shortcut1, shortcut2))
            Log.d("SHORTCUTS", "CREATED $isSuccess")

        } catch (e: Exception) {
            Log.e("SHORTCUTS", "ERROR IN LAYING SHORTCUTS", e)
        }
    }
}