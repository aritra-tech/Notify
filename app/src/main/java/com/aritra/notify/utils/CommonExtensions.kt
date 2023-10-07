package com.aritra.notify.utils

import android.content.Context
import android.widget.Toast
import com.aritra.notify.ui.screens.MainActivity

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
fun Context.closeApp() {
    (this as? MainActivity)?.finish()
}

/**
 * Executes the specified [block] lambda if the receiver value is not null.
 *
 * @param block A lambda expression that takes a non-null receiver value (`T`) as its argument and returns a result of type `R`.
 * @return If the receiver value (`this`) is not null, the [block] lambda is executed with the non-null value, and the result is returned as a non-null value of type `R`. If the receiver value is null, the function returns null.
 *
 * @sample
 * val result: Int? = "42".withNotNull { it.toIntOrNull() } // result will be 42
 * val nullResult: Int? = null.withNotNull { it.toIntOrNull() } // nullResult will be null
 *
 * @param T The type of the receiver value (nullable).
 * @param R The type of the result value (nullable).
 * @receiver The nullable receiver value on which this function is called.
 */
inline fun <T : Any, R> T?.withNotNull(block: (T) -> R): R? {
    return this?.let(block)
}
