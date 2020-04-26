package it.czerwinski.android.charts.core

import android.graphics.Canvas
import java.util.*

/**
 * Draw the text, with origin at _(x, y)_, using the specified `TextPaint`.
 *
 * When drawing text with a `TextPain`, it is advised to use this function instead of `drawText()`.
 *
 * @receiver The canvas
 * @param text The text to be drawn.
 * @param x X coordinate of the origin of the text.
 * @param y Y coordinate of the base line of the text.
 * @param paint The paint used for the text.
 */
fun Canvas.drawTextAdvanced(
    text: String,
    x: Float,
    y: Float,
    paint: TextPaint
) {
    if (paint.textAllCaps) drawText(text.toUpperCase(locale = Locale.getDefault()), x, y, paint)
    else drawText(text, x, y, paint)
}
