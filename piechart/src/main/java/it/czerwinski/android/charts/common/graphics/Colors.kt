package it.czerwinski.android.charts.common.graphics

import android.graphics.Color
import kotlin.math.roundToInt

fun mixColors(color1: Int, color2: Int, ratio: Float): Int =
    Color.argb(
        mixInts(Color.alpha(color1), Color.alpha(color2), ratio),
        mixInts(Color.red(color1), Color.red(color2), ratio),
        mixInts(Color.green(color1), Color.green(color2), ratio),
        mixInts(Color.blue(color1), Color.blue(color2), ratio)
    )

private fun mixInts(int1: Int, int2: Int, ratio: Float): Int =
    (int1 * (1f - ratio) + int2 * ratio).roundToInt()
