package it.czerwinski.android.charts.core

import android.animation.TypeEvaluator

class FloatArrayEvaluator : TypeEvaluator<FloatArray> {

    override fun evaluate(
        fraction: Float,
        startValue: FloatArray,
        endValue: FloatArray
    ): FloatArray = (startValue zip endValue)
        .map { (start, end) ->
            start + fraction * (end - start)
        }
        .toFloatArray()
}