package it.czerwinski.android.charts.common.graphics

import android.graphics.Path
import it.czerwinski.android.charts.common.radToDeg

interface PathProvider : RectFProvider {

    fun path(init: Path.() -> Unit): Path

    fun donutSlice(
        cx: Float,
        cy: Float,
        startAngle: Float,
        endAngle: Float,
        innerRadius: Float,
        outerRadius: Float,
        padding: Float
    ): Path = path {
        val sweepAngle = endAngle - startAngle
        val outerPadding = anglePadding(padding, outerRadius)
        val innerPadding = anglePadding(padding, innerRadius)
        arcTo(
            ovalF(cx, cy, outerRadius),
            startAngle + outerPadding,
            sweepAngle - 2 * outerPadding
        )
        arcTo(
            ovalF(cx, cy, innerRadius),
            endAngle - innerPadding,
            -sweepAngle + 2 * innerPadding
        )
        close()
    }

    private fun anglePadding(padding: Float, radius: Float): Float =
        (padding / radius).radToDeg() / 2f
}
