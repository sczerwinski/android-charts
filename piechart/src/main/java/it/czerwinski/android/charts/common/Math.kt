package it.czerwinski.android.charts.common

internal const val PI: Float = Math.PI.toFloat()
internal const val DOUBLE_PI: Float = PI * 2f

internal const val FULL_ANGLE: Float = 360f

internal fun Float.radToDeg(): Float = this * FULL_ANGLE / DOUBLE_PI
internal fun Float.degToRad(): Float = this * DOUBLE_PI / FULL_ANGLE