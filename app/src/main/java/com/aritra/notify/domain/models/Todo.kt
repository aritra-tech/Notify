package com.aritra.notify.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @property title the title of the todo
 * @property isChecked the status of the todo
 */
@Parcelize
data class Todo(
    val title: String = "",
    val isChecked: Boolean = false,
) : Parcelable
