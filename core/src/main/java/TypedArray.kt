/*
 * Copyright 2018–2020 Android Charts Open Source Project
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

package it.czerwinski.android.charts.core

import android.content.Context
import android.content.res.TypedArray
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import androidx.annotation.StyleableRes

/**
 * Retrieves the interpolator instance for the attribute at [index].
 *
 * If the value does not exist, [defValue] will be returned instead.
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
 * @param indices `@StyleableRes` indices
 * @return
 */
fun TypedArray.findIndexWithValue(@StyleableRes vararg indices: Int): Int =
    indices.find { index -> hasValue(index) } ?: indices.last()
