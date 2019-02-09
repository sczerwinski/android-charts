package it.czerwinski.android.charts.common.graphics

import android.graphics.Rect
import android.graphics.RectF
import kotlin.math.roundToInt

class InSituRectProvider : RectProvider {

    private val value = Rect()

    override fun rect(left: Int, top: Int, right: Int, bottom: Int): Rect =
        value.apply {
            this.left = left
            this.top = top
            this.right = right
            this.bottom = bottom
        }

    override fun rect(rectF: RectF): Rect =
        rect(
            rectF.left.roundToInt(),
            rectF.top.roundToInt(),
            rectF.right.roundToInt(),
            rectF.bottom.roundToInt()
        )

    override fun rect(rect: Rect): Rect =
        rect(rect.left, rect.top, rect.right, rect.bottom)

    override fun oval(cx: Int, cy: Int, radius: Int): Rect =
        rect(left = cx - radius, top = cy - radius, right = cx + radius, bottom = cy + radius)
}