package it.czerwinski.android.charts.common

import android.graphics.Canvas
import kotlin.math.cos
import kotlin.math.sin

internal fun Canvas.translateRadial(distance: Float, angle: Float) {
    translate(distance * cos(angle.degToRad()), distance * sin(angle.degToRad()))
}

internal fun Canvas.translatedRadial(distance: Float, angle: Float, draw: Canvas.() -> Unit) {
    val saveCount = save()
    translateRadial(distance, angle)
    draw()
    restoreToCount(saveCount)
}
