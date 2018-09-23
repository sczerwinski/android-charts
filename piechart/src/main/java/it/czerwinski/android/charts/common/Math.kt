package it.czerwinski.android.charts.common

internal const val PI: Float = Math.PI.toFloat()
internal const val DOUBLE_PI: Float = PI * 2f

internal const val FULL_ANGLE: Float = 360f

internal fun Float.radToDeg(): Float = this * DOUBLE_PI / FULL_ANGLE
internal fun Float.degToRad(): Float = this * FULL_ANGLE / DOUBLE_PI

internal fun Iterable<Float>.partialSums(): List<Float> =
    fold(listOf(0f)) { sums, next ->
        sums + (sums.last() + next)
    }
