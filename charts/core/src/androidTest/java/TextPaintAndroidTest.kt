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

import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import androidx.test.platform.app.InstrumentationRegistry
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import io.mockk.slot
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class TextPaintAndroidTest {

    private val context get() = InstrumentationRegistry.getInstrumentation().targetContext

    @MockK
    lateinit var typedArray: TypedArray

    @MockK
    lateinit var normalTypeface: Typeface

    @MockK
    lateinit var boldTypeface: Typeface

    @MockK
    lateinit var italicTypeface: Typeface

    @MockK
    lateinit var sansBoldTypeface: Typeface

    @MockK
    lateinit var serifBoldTypeface: Typeface

    @MockK
    lateinit var monoBoldTypeface: Typeface

    @MockK
    lateinit var thinTypeface: Typeface

    @MockK
    lateinit var thinItalicTypeface: Typeface

    private var paint = TextPaint()

    @BeforeEach
    fun setUpPaint() {
        paint.textSize = INITIAL_TEXT_SIZE
        paint.color = INITIAL_TEXT_COLOR
    }

    @BeforeEach
    fun setUpTypefaces() {
        mockkStatic(Typeface::class)
        every { Typeface.defaultFromStyle(Typeface.NORMAL) } returns normalTypeface
        every { Typeface.defaultFromStyle(Typeface.BOLD) } returns boldTypeface
        every { Typeface.defaultFromStyle(Typeface.ITALIC) } returns italicTypeface
        every { Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD) } returns sansBoldTypeface
        every { Typeface.create(Typeface.SERIF, Typeface.BOLD) } returns serifBoldTypeface
        every { Typeface.create(Typeface.MONOSPACE, Typeface.BOLD) } returns monoBoldTypeface
        every { Typeface.create(FONT_FAMILY_THIN, Typeface.NORMAL) } returns thinTypeface
        every { Typeface.create(FONT_FAMILY_THIN, Typeface.ITALIC) } returns thinItalicTypeface
        every { Typeface.create(thinTypeface, Typeface.ITALIC) } returns thinItalicTypeface
    }

    @BeforeEach
    fun setUpResourcesCompat() {
        mockkStatic(ResourcesCompat::class)
        every { ResourcesCompat.getFont(context, FONT_FAMILY_THIN_RES_ID) } returns thinTypeface
        every {
            ResourcesCompat.getFont(context, INVALID_FONT_FAMILY_THIN_RES_ID)
        } throws Resources.NotFoundException()
    }

    private fun setUpTypedArray(
        textSize: Float? = null,
        textColor: Int? = null,
        textStyle: Int? = null,
        typeface: Int? = null,
        fontFamily: String? = null,
        fontFamilyResourceId: Int? = null,
        usingAndroidFontFamily: Boolean = false,
        textAllCaps: Boolean? = null
    ) {
        val textSizeSlot = slot<Float>()
        val textColorSlot = slot<Int>()
        val textStyleSlot = slot<Int>()
        val typefaceSlot = slot<Int>()
        val resIdSlot = slot<Int>()
        val textAllCapsSlot = slot<Boolean>()
        every {
            typedArray.getDimension(R.styleable.TextPaint_android_textSize, capture(textSizeSlot))
        } answers { textSize ?: textSizeSlot.captured }
        every {
            typedArray.getColor(R.styleable.TextPaint_android_textColor, capture(textColorSlot))
        } answers { textColor ?: textColorSlot.captured }
        every {
            typedArray.getInt(R.styleable.TextPaint_android_textStyle, capture(textStyleSlot))
        } answers { textStyle ?: textStyleSlot.captured }
        every {
            typedArray.getInt(R.styleable.TextPaint_android_typeface, capture(typefaceSlot))
        } answers { typeface ?: typefaceSlot.captured }
        every {
            typedArray.hasValue(R.styleable.TextPaint_android_fontFamily)
        } returns (usingAndroidFontFamily && (fontFamily != null || fontFamilyResourceId != null))
        every {
            typedArray.getString(R.styleable.TextPaint_android_fontFamily)
        } returns fontFamily.takeIf { usingAndroidFontFamily }
        every {
            typedArray.getResourceId(R.styleable.TextPaint_android_fontFamily, capture(resIdSlot))
        } answers { fontFamilyResourceId.takeIf { usingAndroidFontFamily } ?: resIdSlot.captured }
        every {
            typedArray.hasValue(R.styleable.TextPaint_fontFamily)
        } returns (!usingAndroidFontFamily && (fontFamily != null || fontFamilyResourceId != null))
        every {
            typedArray.getString(R.styleable.TextPaint_fontFamily)
        } returns fontFamily.takeUnless { usingAndroidFontFamily }
        every {
            typedArray.getResourceId(R.styleable.TextPaint_fontFamily, capture(resIdSlot))
        } answers { fontFamilyResourceId.takeUnless { usingAndroidFontFamily } ?: resIdSlot.captured }
        every {
            typedArray.getBoolean(R.styleable.TextPaint_textAllCaps, capture(textAllCapsSlot))
        } answers { textAllCaps ?: textAllCapsSlot.captured }
    }

    @Test
    @DisplayName("Given no style, when applyFrom, then use defaults")
    fun emptyStyle() {
        setUpTypedArray()

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(normalTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given text size only, when applyFrom, then change text size")
    fun textSize() {
        setUpTypedArray(textSize = 20f)

        paint.applyFrom(context, typedArray)

        assertEquals(20f, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(normalTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given text color only, when applyFrom, then change text color")
    fun textColor() {
        setUpTypedArray(textColor = Color.RED)

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(Color.RED, paint.color)
        assertEquals(normalTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given bold text style, when applyFrom, then set bold typeface")
    fun bold() {
        setUpTypedArray(textStyle = Typeface.BOLD)

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(boldTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given sans-serif typeface, when applyFrom, then set sans-serif typeface")
    fun sansSerifTypeface() {
        setUpTypedArray(typeface = TYPEFACE_SANS_SERIF)

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(Typeface.SANS_SERIF, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given bold text style and sans-serif typeface, when applyFrom, " +
            "then set bold sans-serif typeface")
    fun boldSansSerifTypeface() {
        setUpTypedArray(textStyle = Typeface.BOLD, typeface = TYPEFACE_SANS_SERIF)

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(sansBoldTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given serif typeface, when applyFrom, then set serif typeface")
    fun serifTypeface() {
        setUpTypedArray(typeface = TYPEFACE_SERIF)

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(Typeface.SERIF, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given bold text style and serif typeface, when applyFrom, " +
            "then set bold serif typeface")
    fun boldSerifTypeface() {
        setUpTypedArray(textStyle = Typeface.BOLD, typeface = TYPEFACE_SERIF)

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(serifBoldTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given monospace typeface, when applyFrom, then set monospace typeface")
    fun monospaceTypeface() {
        setUpTypedArray(typeface = TYPEFACE_MONOSPACE)

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(Typeface.MONOSPACE, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given bold text style and monospace typeface, when applyFrom, " +
            "then set bold monospace typeface")
    fun boldMonospaceTypeface() {
        setUpTypedArray(textStyle = Typeface.BOLD, typeface = TYPEFACE_MONOSPACE)

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(monoBoldTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given font family, when applyFrom, then set typeface from font family")
    fun fontFamily() {
        setUpTypedArray(fontFamily = FONT_FAMILY_THIN)

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(thinTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given font family with android namespace, when applyFrom, " +
            "then set typeface from font family")
    fun androidFontFamily() {
        setUpTypedArray(fontFamily = FONT_FAMILY_THIN, usingAndroidFontFamily = true)

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(thinTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given font family resource ID, when applyFrom, " +
            "then set typeface from font family")
    fun fontFamilyResId() {
        setUpTypedArray(fontFamilyResourceId = FONT_FAMILY_THIN_RES_ID)

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(thinTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given font family resource ID with android namespace, when applyFrom, " +
            "then set typeface from font family")
    fun androidFontFamilyResId() {
        setUpTypedArray(
            fontFamilyResourceId = FONT_FAMILY_THIN_RES_ID,
            usingAndroidFontFamily = true
        )

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(thinTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given italic text style and font family, when applyFrom, " +
            "then set italic typeface from font family")
    fun italicFontFamily() {
        setUpTypedArray(textStyle = Typeface.ITALIC, fontFamily = FONT_FAMILY_THIN)

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(thinItalicTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given italic text style and font family with android namespace, " +
            "when applyFrom, then set italic typeface from font family")
    fun italicAndroidFontFamily() {
        setUpTypedArray(
            textStyle = Typeface.ITALIC,
            fontFamily = FONT_FAMILY_THIN,
            usingAndroidFontFamily = true
        )

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(thinItalicTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given italic text style and font family resource ID, " +
            "when applyFrom, then set italic typeface from font family")
    fun italicFontFamilyResId() {
        setUpTypedArray(
            textStyle = Typeface.ITALIC,
            fontFamilyResourceId = FONT_FAMILY_THIN_RES_ID
        )

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(thinItalicTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given italic text style and font family resource ID " +
            "with android namespace, when applyFrom, then set italic typeface from font family")
    fun italicAndroidFontFamilyResId() {
        setUpTypedArray(
            textStyle = Typeface.ITALIC,
            fontFamilyResourceId = FONT_FAMILY_THIN_RES_ID,
            usingAndroidFontFamily = true
        )

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(thinItalicTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given italic text style and invalid font family resource ID, " +
            "when applyFrom, then set italic typeface")
    fun italicInvalidFontFamilyResId() {
        setUpTypedArray(
            textStyle = Typeface.ITALIC,
            fontFamilyResourceId = INVALID_FONT_FAMILY_THIN_RES_ID
        )

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(italicTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given italic text style and invalid font family resource ID " +
            "with android namespace, when applyFrom, then set italic typeface")
    fun italicInvalidAndroidFontFamilyResId() {
        setUpTypedArray(
            textStyle = Typeface.ITALIC,
            fontFamilyResourceId = INVALID_FONT_FAMILY_THIN_RES_ID,
            usingAndroidFontFamily = true
        )

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(italicTypeface, paint.typeface)
        assertFalse(paint.textAllCaps)
    }

    @Test
    @DisplayName("Given text all caps only, when applyFrom, then set to all caps")
    fun textAllCaps() {
        setUpTypedArray(textAllCaps = true)

        paint.applyFrom(context, typedArray)

        assertEquals(INITIAL_TEXT_SIZE, paint.textSize)
        assertEquals(INITIAL_TEXT_COLOR, paint.color)
        assertEquals(normalTypeface, paint.typeface)
        assertTrue(paint.textAllCaps)
    }

    companion object {
        const val FONT_FAMILY_THIN = "thin"
        const val FONT_FAMILY_THIN_RES_ID = 0x7412
        const val INVALID_FONT_FAMILY_THIN_RES_ID = 0xe0e0
        const val INITIAL_TEXT_SIZE = 10f
        const val INITIAL_TEXT_COLOR = Color.BLACK
        const val TYPEFACE_SANS_SERIF = 1
        const val TYPEFACE_SERIF = 2
        const val TYPEFACE_MONOSPACE = 3
    }
}
