package com.aritradas.notify.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlin.streams.toList

@Composable
fun TypewriterText(
    texts: List<String>,
    modifier: Modifier = Modifier,
) {
    var textIndex by remember {
        mutableIntStateOf(0)
    }
    var textToDisplay by remember {
        mutableStateOf("")
    }
    val textCharsList: List<List<String>> = remember {
        texts.map {
            it.splitToCodePoints()
        }
    }

    LaunchedEffect(key1 = texts) {
        while (textIndex < textCharsList.size) {
            textCharsList[textIndex].forEachIndexed { charIndex, _ ->
                textToDisplay = textCharsList[textIndex]
                    .take(
                        n = charIndex + 1
                    ).joinToString(
                        separator = ""
                    )
                delay(160)
            }
            textIndex = (textIndex + 1) % texts.size
            delay(1000)
        }
    }

    Text(
        modifier = modifier,
        text = textToDisplay
    )
}

fun String.splitToCodePoints(): List<String> {
    return codePoints()
        .toList()
        .map {
            String(Character.toChars(it))
        }
}
