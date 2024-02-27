package com.aritra.notify.ui.theme

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn

val FadeIn = fadeIn(animationSpec = tween(220, delayMillis = 90)) + scaleIn(
    initialScale = 0.92f,
    animationSpec = tween(
        220, delayMillis = 90
    )
)

val FadeOut = fadeOut(animationSpec = tween(90))
