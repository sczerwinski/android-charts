package it.czerwinski.android.charts.common.graphics

import android.graphics.Rect
import android.graphics.RectF

class InSituRectFProvider : RectFProvider {

    private val value = RectF()

    override fun rectF(left: Float, top: Float, right: Float, bottom: Float): RectF =
        value.apply {
            this.left = left
            this.top = top
            this.right = right
            this.bottom = bottom
        }

    override fun rectF(rectF: RectF): RectF =
        rectF(rectF.left, rectF.top, rectF.right, rectF.bottom)

    override fun rectF(rect: Rect): RectF =
        rectF(rect.left.toFloat(), rect.top.toFloat(), rect.right.toFloat(), rect.bottom.toFloat())

    override fun ovalF(cx: Float, cy: Float, radius: Float): RectF =
        rectF(left = cx - radius, top = cy - radius, right = cx + radius, bottom = cy + radius)
}