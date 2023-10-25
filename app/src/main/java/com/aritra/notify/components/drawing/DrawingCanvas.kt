package com.aritra.notify.components.drawing

import android.graphics.Canvas
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInteropFilter
import com.aritra.notify.utils.absoluteValue
import com.aritra.notify.utils.pos

typealias DrawableFactory = (DrawableProperties) -> Drawable
typealias ColorFactory = () -> Color

private const val TOUCH_TOLERANCE = 4
private val stroke = Stroke(
    width = 10f,
    cap = StrokeCap.Round,
    join = StrokeJoin.Round
)

/**
 * The current offset of the finger
 */
private var currentOffset = 0 pos 0

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawingCanvas(
    modifier: Modifier = Modifier,
    drawableFactory: DrawableFactory,
    colorFactory: ColorFactory,
) {
    val drawables = remember { mutableListOf<Drawable>() }
    var invalidate by remember { mutableIntStateOf(0) }
    var canvas by remember { mutableStateOf<Canvas?>(null) }

    LaunchedEffect(Unit) {
        // reset the current offset when the composable is first launched
        currentOffset = 0 pos 0
    }

    val touchStart: (Offset) -> Unit = remember(drawableFactory, colorFactory) {
        { offset ->
            // save the current coordinates of the finger
            currentOffset = offset
            // create a new drawable and add it to the list
            drawables += drawableFactory(DrawableProperties(offset, offset, stroke, colorFactory()))
        }
    }

    val touchMove: (Offset) -> Unit = remember {
        { offset ->
            val change = (offset - currentOffset).absoluteValue()

            if (change.x >= TOUCH_TOLERANCE || change.y >= TOUCH_TOLERANCE) {
                currentOffset = offset
                drawables.lastOrNull()?.end = offset
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        touchStart(event.x pos event.y)
                        invalidate++
                        true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        touchMove(event.x pos event.y)
                        invalidate++
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        invalidate++
                        true
                    }

                    else -> false
                }
            },
        onDraw = {
            if (canvas == null) {
                canvas = drawContext.canvas.nativeCanvas
            }
            // invalidate is called whenever a new stroke is added so the canvas is redrawn
            invalidate.let {
                // iterate over each stroke and draw it on the canvas
                drawables.forEach { it.draw(this) }
            }
        }
    )
}
