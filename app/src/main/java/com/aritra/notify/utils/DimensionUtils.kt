package com.aritra.notify.utils

import android.content.res.Resources
import android.util.TypedValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import kotlin.math.absoluteValue

/**
 * @param resources the android resource of an activity or fragment
 *
 * @return the dp equivalent of this number in int
 * */
fun Number.dp(resources: Resources) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    toFloat(),
    resources.displayMetrics
).toInt()

infix fun Number.pos(other: Number) = Offset(toFloat(), other.toFloat())

fun Offset.size() = Size(x, y)
fun Offset.absoluteValue() = Offset(x.absoluteValue, y.absoluteValue)
