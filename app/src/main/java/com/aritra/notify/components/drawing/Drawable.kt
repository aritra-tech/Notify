package com.aritra.notify.components.drawing

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.aritra.notify.utils.pos
import com.aritra.notify.utils.size
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

/**
 * Specifies that the class can draw something
 */
abstract class Drawable(
    val start: Offset,
    open var end: Offset,
    val stroke: Stroke,
    val color: Color,
) {

    constructor(props: DrawableProperties) : this(
        props.start,
        props.end,
        props.stroke,
        props.color
    )

    /**
     * Draws the drawable on the canvas
     *
     * @param scope the object used to draw the drawable
     * */
    abstract fun draw(scope: DrawScope)

    override fun toString() = "${this::class.simpleName} from start: $start to end: $end"
}

data class DrawableProperties(
    val start: Offset,
    val end: Offset,
    val stroke: Stroke,
    val color: Color,
)

class Pencil(properties: DrawableProperties) : Drawable(properties) {
    private val path = Path()
    override var end: Offset = start.x pos start.y
        set(value) {
            path.quadraticBezierTo(
                end.x,
                end.y,
                ((end.x + value.x) / 2),
                ((end.y + value.y) / 2)
            )
            path.lineTo(value.x, value.y)
            field = value
        }

    init {
        path.moveTo(start.x, start.y)
    }

    override fun draw(scope: DrawScope) {
        scope.drawPath(path, color, style = stroke)
    }
}

open class Line(properties: DrawableProperties) : Drawable(properties) {

    override fun draw(scope: DrawScope) {
        scope.drawLine(
            color,
            start,
            end,
            stroke.width,
            stroke.cap,
            stroke.pathEffect
        )
    }
}

class Arrow(properties: DrawableProperties) : Line(properties) {

    override fun draw(scope: DrawScope) {
        // calls the super method to draw the line
        super.draw(scope)
        // draw the arrow head
        val (radius, angle) = 30f to 60f
        val angleInRad = PI * angle / 180f
        val lineAngle = atan2(end.y - start.y, end.x - start.x)
        scope.drawPath(
            Path().apply {
                fillType = PathFillType.EvenOdd
                moveTo(end.x, end.y)
                lineTo(
                    (end.x - radius * cos(lineAngle - (angleInRad / 2))).toFloat(),
                    (end.y - radius * sin(lineAngle - (angleInRad / 2))).toFloat()
                )
                moveTo(end.x, end.y)
                lineTo(
                    (end.x - radius * cos(lineAngle + (angleInRad / 2))).toFloat(),
                    (end.y - radius * sin(lineAngle + (angleInRad / 2))).toFloat()
                )
            },
            color,
            style = stroke
        )
    }
}

class Rectangle(properties: DrawableProperties) : Drawable(properties) {

    override fun draw(scope: DrawScope) {
        scope.drawRect(color, start, (end - start).size(), style = stroke)
    }
}

class Oval(properties: DrawableProperties) : Drawable(properties) {

    override fun draw(scope: DrawScope) {
        scope.drawOval(color, start, (end - start).size(), style = stroke)
    }
}
