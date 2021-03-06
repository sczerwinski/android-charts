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
import android.content.res.ColorStateList
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class TypedArrayTest {

    @MockK
    lateinit var context: Context

    @MockK
    lateinit var typedArray: TypedArray

    @MockK
    lateinit var interpolator: Interpolator

    @MockK
    lateinit var defaultInterpolator: Interpolator

    @Test
    fun `Given typed array of attributes, when indices, then return indices of attributes`() {
        every { typedArray.indexCount } returns 2
        every { typedArray.getIndex(0) } returns FIRST_INDEX
        every { typedArray.getIndex(1) } returns SECOND_INDEX

        val result = typedArray.indices

        assertEquals(listOf(FIRST_INDEX, SECOND_INDEX), result.toList())
    }

    @Test
    fun `Given typed array resource, when indices, then return ordinal indices`() {
        every { typedArray.indexCount } returns 0
        every { typedArray.length() } returns 2

        val result = typedArray.indices

        assertEquals(listOf(0, 1), result.toList())
    }

    @Test
    fun `Given interpolator was not set, when getInterpolator, then return default interpolator`() {
        every { typedArray.getResourceId(INTERPOLATOR_ATTR_INDEX, 0) } returns 0

        val result = typedArray.getInterpolator(
            context = context,
            index = INTERPOLATOR_ATTR_INDEX,
            defValue = defaultInterpolator
        )

        assertSame(defaultInterpolator, result)
    }

    @Test
    fun `Given interpolator loads successfully, when getInterpolator, then return loaded interpolator`() {
        every {
            typedArray.getResourceId(INTERPOLATOR_ATTR_INDEX, 0)
        } returns INTERPOLATOR_RES_ID
        mockkStatic(AnimationUtils::class)
        every {
            AnimationUtils.loadInterpolator(context, INTERPOLATOR_RES_ID)
        } returns interpolator

        val result = typedArray.getInterpolator(
            context = context,
            index = INTERPOLATOR_ATTR_INDEX,
            defValue = defaultInterpolator
        )

        assertSame(interpolator, result)
    }

    @Test
    fun `Given loading interpolator fails, when getInterpolator, then return default interpolator`() {
        every {
            typedArray.getResourceId(INTERPOLATOR_ATTR_INDEX, 0)
        } returns INTERPOLATOR_RES_ID
        mockkStatic(AnimationUtils::class)
        every {
            AnimationUtils.loadInterpolator(context, INTERPOLATOR_RES_ID)
        } throws Resources.NotFoundException()

        val result = typedArray.getInterpolator(
            context = context,
            index = INTERPOLATOR_ATTR_INDEX,
            defValue = defaultInterpolator
        )

        assertSame(defaultInterpolator, result)
    }

    @Test
    fun `Given all indices have values, when findIndexWithValue, then return first index`() {
        every { typedArray.hasValue(any()) } returns true

        val result = typedArray.findIndexWithValue(FIRST_INDEX, SECOND_INDEX, THIRD_INDEX)

        assertEquals(FIRST_INDEX, result)
    }

    @Test
    fun `Given only first index has value, when findIndexWithValue, then return first index`() {
        every { typedArray.hasValue(FIRST_INDEX) } returns true
        every { typedArray.hasValue(SECOND_INDEX) } returns false

        val result = typedArray.findIndexWithValue(FIRST_INDEX, SECOND_INDEX)

        assertEquals(FIRST_INDEX, result)
    }

    @Test
    fun `Given only second index has value, when findIndexWithValue, then return second index`() {
        every { typedArray.hasValue(FIRST_INDEX) } returns false
        every { typedArray.hasValue(SECOND_INDEX) } returns true

        val result = typedArray.findIndexWithValue(FIRST_INDEX, SECOND_INDEX)

        assertEquals(SECOND_INDEX, result)
    }

    @Test
    fun `Given no index has value, when findIndexWithValue, then return last index`() {
        every { typedArray.hasValue(any()) } returns false

        val result = typedArray.findIndexWithValue(FIRST_INDEX, SECOND_INDEX, THIRD_INDEX)

        assertEquals(THIRD_INDEX, result)
    }

    @Test
    fun `Given attribute is array of colors, when getColors, then return array of colors`() {
        every { typedArray.getResourceId(COLORS_ATTR_INDEX, any()) } returns COLORS_RES_ID
        every { context.resources.obtainTypedArray(COLORS_RES_ID) } returns COLORS_TYPED_ARRAY
        every { COLORS_TYPED_ARRAY.indexCount } returns 0
        every { COLORS_TYPED_ARRAY.length() } returns 3
        every { COLORS_TYPED_ARRAY.getColorStateList(0) } returns COLOR_1
        every { COLORS_TYPED_ARRAY.getColorStateList(1) } returns COLOR_2
        every { COLORS_TYPED_ARRAY.getColorStateList(2) } returns COLOR_3

        val result = typedArray.getColors(context, COLORS_ATTR_INDEX, Color.BLACK)

        assertEquals(listOf(COLOR_1, COLOR_2, COLOR_3), result)
    }

    @Test
    fun `Given attribute is a single color, when getColors, then return array containing the color`() {
        every { typedArray.getResourceId(COLORS_ATTR_INDEX, any()) } returns COLORS_RES_ID
        every { context.resources.obtainTypedArray(COLORS_RES_ID) } throws Resources.NotFoundException()
        every { typedArray.getColorStateList(COLORS_ATTR_INDEX) } returns COLOR_1

        val result = typedArray.getColors(context, COLORS_ATTR_INDEX, Color.BLACK)

        assertEquals(listOf(COLOR_1), result)
    }

    @Test
    fun `Given attribute is of invalid type, when getColors, then return array containing default color`() {
        every { typedArray.getResourceId(COLORS_ATTR_INDEX, any()) } returns COLORS_RES_ID
        every { context.resources.obtainTypedArray(COLORS_RES_ID) } throws Resources.NotFoundException()
        every { typedArray.getColorStateList(COLORS_ATTR_INDEX) } throws UnsupportedOperationException()
        mockkStatic(ColorStateList::class)
        every { ColorStateList.valueOf(Color.BLACK) } returns COLOR_3

        val result = typedArray.getColors(context, COLORS_ATTR_INDEX, Color.BLACK)

        assertEquals(listOf(COLOR_3), result)
    }

    @Test
    fun `Given attribute is not set, when getColors, then return array containing default color`() {
        every { typedArray.getResourceId(COLORS_ATTR_INDEX, any()) } returns 0
        every { typedArray.getColorStateList(COLORS_ATTR_INDEX) } returns null
        mockkStatic(ColorStateList::class)
        every { ColorStateList.valueOf(Color.BLACK) } returns COLOR_3

        val result = typedArray.getColors(context, COLORS_ATTR_INDEX, Color.BLACK)

        assertEquals(listOf(COLOR_3), result)
    }

    companion object {

        const val INTERPOLATOR_ATTR_INDEX = 1
        const val INTERPOLATOR_RES_ID = 0x1234

        const val FIRST_INDEX = 21
        const val SECOND_INDEX = 22
        const val THIRD_INDEX = 23

        const val COLORS_ATTR_INDEX = 1
        const val COLORS_RES_ID = 0x1234
        val COLORS_TYPED_ARRAY = mockk<TypedArray>(relaxUnitFun = true)
        val COLOR_1 = mockk<ColorStateList>(name = "Color 1")
        val COLOR_2 = mockk<ColorStateList>(name = "Color 2")
        val COLOR_3 = mockk<ColorStateList>(name = "Color 3")
    }
}
