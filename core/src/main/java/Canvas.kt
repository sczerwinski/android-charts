/*
 * Copyright 2018–2020 Android Charts Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
