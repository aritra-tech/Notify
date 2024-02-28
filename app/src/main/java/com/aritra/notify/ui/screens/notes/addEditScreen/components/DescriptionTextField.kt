package com.aritra.notify.ui.screens.notes.addEditScreen.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.aritra.notify.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescriptionTextField(
    modifier: Modifier = Modifier,
    scrollOffset: Int,
    contentSize: Int,
    description: String,
    parentScrollState: ScrollState,
    isNewNote: Boolean = false,
    onDescriptionChange: (String) -> Unit,
) {
    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(Unit) {
        if (isNewNote) {
            focusRequester.requestFocus()
        }
    }
    var descriptionFieldValue by remember {
        mutableStateOf(TextFieldValue(description))
    }
    remember(description) {
        descriptionFieldValue = descriptionFieldValue.copy(text = description)
        Any()
    }
    var descriptionLayoutResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    val lineNumberAtCursor by remember {
        derivedStateOf {
            descriptionLayoutResult?.getLineForOffset(descriptionFieldValue.selection.start)
        }
    }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = descriptionFieldValue.text) {
        scope.launch {
            val cursorLineNumber = lineNumberAtCursor ?: 0
            val firstVisibleLinePosition = (parentScrollState.value - scrollOffset).toFloat()
            val lastVisibleLinePosition = (parentScrollState.value + contentSize - scrollOffset).toFloat()
            val firstVisibleLine = descriptionLayoutResult?.getLineForVerticalPosition(firstVisibleLinePosition) ?: 0
            val lastLineVisible = descriptionLayoutResult?.getLineForVerticalPosition(lastVisibleLinePosition).let {
                ((it ?: 0) - 1).coerceAtLeast(0) // decrement by 1 to get last completely visible line
            }

            if (cursorLineNumber < firstVisibleLine) {
                val cursorLineTop = descriptionLayoutResult?.getLineTop(cursorLineNumber) ?: 0f
                val firstLineTop = descriptionLayoutResult?.getLineTop(firstVisibleLine) ?: 0f
                val diff = cursorLineTop - firstLineTop
                parentScrollState.animateScrollBy(diff, spring(stiffness = Spring.StiffnessMediumLow))
            } else if (cursorLineNumber >= lastLineVisible) {
                val cursorLineBottom = descriptionLayoutResult?.getLineBottom(cursorLineNumber) ?: 0f
                val lastLineTop = descriptionLayoutResult?.getLineTop(lastLineVisible) ?: 0f
                val diff = cursorLineBottom - lastLineTop
                parentScrollState.animateScrollBy(diff, spring(stiffness = Spring.StiffnessMediumLow))
            }
        }
    }

    val interactionSource = remember { MutableInteractionSource() }

    BasicTextField(
        modifier = modifier
            .fillMaxSize()
            .focusRequester(focusRequester),
        value = descriptionFieldValue,
        onValueChange = { newDescription ->
            descriptionFieldValue = newDescription
            onDescriptionChange(newDescription.text)
        },
        onTextLayout = {
            descriptionLayoutResult = it
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            fontFamily = FontFamily(Font(R.font.poppins_light))
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = KeyboardType.Text
        ),
        interactionSource = interactionSource,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = descriptionFieldValue.text,
                visualTransformation = VisualTransformation.None,
                innerTextField = innerTextField,
                placeholder = {
                    Text(
                        stringResource(R.string.notes),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W500,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.poppins_light))
                    )
                },
                singleLine = false,
                enabled = true,
                interactionSource = interactionSource,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    disabledContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }
    )
}
