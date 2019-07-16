/*
 * Copyright 2018–2019 Android Charts Open Source Project
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
import android.util.AttributeSet
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.annotation.StyleableRes

fun View.withStyledAttributes(
    attrs: AttributeSet?,
    @StyleableRes stylables: IntArray,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
    initAttrs: TypedArray.() -> Unit
) {
    context?.withStyledAttributes(attrs, stylables, defStyleAttr, defStyleRes, initAttrs)
}

fun Context.withStyledAttributes(
    attrs: AttributeSet?,
    @StyleableRes stylables: IntArray,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
    initAttrs: TypedArray.() -> Unit
) {
    val attrsArray = obtainStyledAttributes(attrs, stylables, defStyleAttr, defStyleRes)
    if (attrsArray != null) {
        attrsArray.initAttrs()
        attrsArray.recycle()
    }
}