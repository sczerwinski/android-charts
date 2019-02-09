package it.czerwinski.android.charts.common.graphics

import android.graphics.Rect
import android.graphics.RectF

interface RectProvider {
    fun rect(left: Int, top: Int, right: Int, bottom: Int): Rect
    fun rect(rect: Rect): Rect
    fun rect(rectF: RectF): Rect
    fun oval(cx: Int, cy: Int, radius: Int): Rect
}
