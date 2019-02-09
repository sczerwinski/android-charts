package it.czerwinski.android.charts.common.graphics

const val PI: Float = Math.PI.toFloat()
const val DOUBLE_PI: Float = PI * 2f

const val FULL_ANGLE: Float = 360f

fun Float.radToDeg(): Float = this * FULL_ANGLE / DOUBLE_PI
fun Float.degToRad(): Float = this * DOUBLE_PI / FULL_ANGLE
