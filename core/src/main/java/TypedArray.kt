package it.czerwinski.android.charts.core

import android.content.Context
import android.content.res.TypedArray
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator

fun TypedArray.getInterpolator(
    context: Context,
    index: Int,
    defValue: Interpolator
): Interpolator = try {
    val interpolatorId = getResourceId(index, 0)
    if (interpolatorId == 0) defValue
    else AnimationUtils.loadInterpolator(context, interpolatorId)
} catch (throwable: Throwable) {
    defValue
}
