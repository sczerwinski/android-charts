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
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface

/**
 * An extension of `Paint` class, allowing for easy style application.
 */
class TextPaint : Paint(ANTI_ALIAS_FLAG) {

    /**
     * Applies a style from the [typedArray] in a given [context] to the `Paint` object.
     */
    fun applyFrom(context: Context?, typedArray: TypedArray) {
        typedArray.initAttrs(context)
    }

    private fun TypedArray.initAttrs(context: Context?) {
        textSize = getDimension(R.styleable.TextPaint_android_textSize, textSize)
        color = getColor(R.styleable.TextPaint_android_textColor, Color.BLACK)
        textScaleX = getFloat(R.styleable.TextPaint_android_textScaleX, 1f)
        textSkewX = getFloat(R.styleable.TextPaint_textPaint_textSkewX, 0f)
        isFakeBoldText = getBoolean(R.styleable.TextPaint_textPaint_bold, false)
        isStrikeThruText = getBoolean(R.styleable.TextPaint_textPaint_strikeThrough, false)
        isUnderlineText = getBoolean(R.styleable.TextPaint_textPaint_underline, false)
        typeface = when (getInt(R.styleable.TextPaint_textPaint_typeface, 0)) {
            TYPEFACE_DEFAULT_BOLD -> Typeface.DEFAULT_BOLD
            TYPEFACE_SANS_SERIF -> Typeface.SANS_SERIF
            TYPEFACE_SERIF -> Typeface.SERIF
            TYPEFACE_MONOSPACE -> Typeface.MONOSPACE
            TYPEFACE_CUSTOM -> try {
                Typeface.createFromAsset(
                    context?.assets,
                    getString(R.styleable.TextPaint_textPaint_typefaceAsset)
                )
            } catch (ignored: Throwable) {
                Typeface.DEFAULT
            }
            else -> Typeface.DEFAULT
        }
    }

    companion object {
        private const val TYPEFACE_DEFAULT = 0
        private const val TYPEFACE_DEFAULT_BOLD = 1
        private const val TYPEFACE_SANS_SERIF = 2
        private const val TYPEFACE_SERIF = 3
        private const val TYPEFACE_MONOSPACE = 4
        private const val TYPEFACE_CUSTOM = 5
    }
}
