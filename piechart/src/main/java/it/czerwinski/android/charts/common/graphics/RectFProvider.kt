package it.czerwinski.android.charts.common.graphics

import android.graphics.Rect
import android.graphics.RectF

interface RectFProvider {
    fun rectF(left: Float, top: Float, right: Float, bottom: Float): RectF
    fun rectF(rectF: RectF): RectF
    fun rectF(rect: Rect): RectF
    fun ovalF(cx: Float, cy: Float, radius: Float): RectF
}
