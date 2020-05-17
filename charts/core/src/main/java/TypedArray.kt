/*
 * Copyright 2018–2020 Slawomir Czerwinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName(name = "TypedArray")

package it.czerwinski.android.charts.core

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.annotation.ColorInt
import androidx.annotation.StyleableRes

/**
 * Indices with data in this array.
 */
val TypedArray.indices: Iterable<Int>
    get() =
        if (indexCount == 0) 0 until length()
        else (0 until indexCount).map(this::getIndex)

/**
 * Retrieves the interpolator instance for the attribute at the given index.
 *
 * If the value does not exist, `defValue` will be returned instead.
 *
 * @receiver A typed array of attributes.
 * @param context The context.
 * @param index Index of attribute to retrieve.
 * @param defValue Default interpolator value to be used.
 */
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

/**
 * Retrieves first of the `@StyleableRes` indices that contains value in this `TypedArray`.
 *
 * If none of the indices contains any value, the last index is returned.
 *
 * @receiver A typed array of attributes.
 * @param indices `@StyleableRes` indices.
 * @return First of the indices containing a value.
 */
fun TypedArray.findIndexWithValue(@StyleableRes vararg indices: Int): Int =
    indices.find { index -> hasValue(index) } ?: indices.last()

/**
 * Retrieves a list of `ColorStateList` objects for the attribute at the given index.
 *
 * @receiver A typed array of attributes.
 * @param context The context.
 * @param index Index of the color attribute.
 * @param defValue Default color value.
 * @return List of `ColorStateList` objects at the given index.
 */
fun TypedArray.getColors(
    context: Context,
    index: Int,
    @ColorInt defValue: Int
): List<ColorStateList> {
    val resourceId = getResourceId(index, 0).takeUnless { it == 0 }

    if (resourceId != null) {
        try {
            val typedArray = context.resources?.obtainTypedArray(resourceId)
            if (typedArray != null) {
                val colors = typedArray.indices.mapNotNull(typedArray::getColorStateList)
                typedArray.recycle()
                if (colors.isNotEmpty()) return colors
            }
            typedArray?.recycle()
        } catch (e: Throwable) {
        }
    }

    val colorStateList = try {
        getColorStateList(index) ?: ColorStateList.valueOf(defValue)
    } catch (e: Throwable) {
        ColorStateList.valueOf(defValue)
    }
    return listOf(colorStateList)
}
