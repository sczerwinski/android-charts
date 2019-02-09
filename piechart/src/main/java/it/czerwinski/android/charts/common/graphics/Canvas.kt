package it.czerwinski.android.charts.common.graphics

import android.graphics.Canvas
import kotlin.math.cos
import kotlin.math.sin

inline fun Canvas.translated(
    dx: Float,
    dy: Float,
    draw: Canvas.() -> Unit
) {
    val saveCount = save()
    translate(dx, dy)
    draw()
    restoreToCount(saveCount)
}

inline fun Canvas.translatedRadial(
    distance: Float,
    angle: Float,
    draw: Canvas.() -> Unit
) {
    val saveCount = save()
    translateRadial(distance, angle)
    draw()
    restoreToCount(saveCount)
}

fun Canvas.translateRadial(distance: Float, angle: Float) {
    translate(distance * cos(angle.degToRad()), distance * sin(angle.degToRad()))
}

inline fun Canvas.scaled(
    sx: Float,
    sy: Float,
    draw: Canvas.() -> Unit
) {
    val saveCount = save()
    scale(sx, sy)
    draw()
    restoreToCount(saveCount)
}

inline fun Canvas.scaled(
    scale: Float,
    draw: Canvas.() -> Unit
) {
    scaled(scale, scale, draw)
}

inline fun Canvas.rotated(
    degrees: Float,
    draw: Canvas.() -> Unit
) {
    val saveCount = save()
    rotate(degrees)
    draw()
    restoreToCount(saveCount)
}

inline fun Canvas.skewed(
    sx: Float,
    sy: Float,
    draw: Canvas.() -> Unit
) {
    val saveCount = save()
    skew(sx, sy)
    draw()
    restoreToCount(saveCount)
}
