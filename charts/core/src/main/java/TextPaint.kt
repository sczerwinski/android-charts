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

package it.czerwinski.android.charts.core

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Paint
import android.graphics.Typeface
import android.util.Log
import androidx.core.content.res.ResourcesCompat

/**
 * An extension of `Paint` class, allowing for easy style application.
 */
class TextPaint : Paint(ANTI_ALIAS_FLAG) {

    /**
     * Indicates whether the text should be drawn in all caps.
     */
    var textAllCaps: Boolean = false

    /**
     * Applies a style from the [typedArray] in a given [context] to the `Paint` object.
     */
    fun applyFrom(context: Context?, typedArray: TypedArray) {
        typedArray.initAttrs(context)
    }

    private fun TypedArray.initAttrs(context: Context?) {
        textSize = getDimension(R.styleable.TextPaint_android_textSize, textSize)
        color = getColor(R.styleable.TextPaint_android_textColor, color)
        textAllCaps = getBoolean(R.styleable.TextPaint_textAllCaps, false)

        val fontFamilyIndex = findIndexWithValue(
            R.styleable.TextPaint_android_fontFamily,
            R.styleable.TextPaint_fontFamily
        )
        setTypeface(
            context = context,
            fontFamilyResourceId = getResourceId(fontFamilyIndex, 0),
            fontFamily = getString(fontFamilyIndex),
            textStyle = getInt(R.styleable.TextPaint_android_textStyle, Typeface.NORMAL),
            typefaceFromId =
            when (getInt(R.styleable.TextPaint_android_typeface, TYPEFACE_DEFAULT)) {
                TYPEFACE_SANS_SERIF -> Typeface.SANS_SERIF
                TYPEFACE_SERIF -> Typeface.SERIF
                TYPEFACE_MONOSPACE -> Typeface.MONOSPACE
                else -> null
            }
        )
    }

    private fun setTypeface(
        context: Context?,
        fontFamilyResourceId: Int,
        fontFamily: String?,
        textStyle: Int,
        typefaceFromId: Typeface?
    ) {
        if (context != null && fontFamilyResourceId != 0) {
            try {
                val typefaceFromFont = ResourcesCompat.getFont(context, fontFamilyResourceId)
                typeface =
                    if (textStyle > 0) Typeface.create(typefaceFromFont, textStyle)
                    else typefaceFromFont
                return
            } catch (e: Resources.NotFoundException) {
                Log.e(TAG, "Could not find font at ID $fontFamilyResourceId", e)
            }
        }
        typeface = if (fontFamily != null) {
            Typeface.create(fontFamily, textStyle)
        } else if (typefaceFromId != null) {
            if (textStyle > 0) Typeface.create(typefaceFromId, textStyle)
            else typefaceFromId
        } else {
            Typeface.defaultFromStyle(textStyle)
        }
    }

    companion object {

        private const val TAG: String = "TextPaint"

        private const val TYPEFACE_DEFAULT = 0
        private const val TYPEFACE_SANS_SERIF = 1
        private const val TYPEFACE_SERIF = 2
        private const val TYPEFACE_MONOSPACE = 3
    }
}
