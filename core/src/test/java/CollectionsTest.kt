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

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class CollectionsTest {

    @ParameterizedTest(name = "{0}.normalize() = {1}")
    @MethodSource("normalizeData")
    fun `When normalize, then return a list with sum of 1`(
        input: List<Float>,
        normalizedList: List<Float>
    ) {
        val result = input.normalize()

        assertArrayEquals(normalizedList.toFloatArray(), result.toFloatArray(), DELTA)
    }

    @ParameterizedTest(name = "{0}.partialSums() = {1}")
    @MethodSource("partialSumsData")
    fun `When partialSums, then return a list of partial sums for the original iterable`(
        input: List<Float>,
        sums: List<Float>
    ) {
        val result = input.partialSums()

        assertArrayEquals(sums.toFloatArray(), result.toFloatArray(), DELTA)
    }

    @ParameterizedTest(name = "{0}.withSize({1}, valueIfEmpty = {2}) = {3}")
    @MethodSource("withSizeData")
    fun `When withSize, then return a list of the new size`(
        input: List<Int>,
        size: Int,
        valueIfEmpty: Int,
        resizedList: List<Int>
    ) {
        val result = input.withSize(size, valueIfEmpty)

        assertEquals(resizedList, result)
    }

    companion object TestData {

        @JvmStatic
        fun normalizeData(): Collection<Array<out Any>> = listOf(
            arrayOf(listOf(10f, 20f, 30f, 40f), listOf(0.1f, 0.2f, 0.3f, 0.4f)),
            arrayOf(listOf(1f, 1f, 1f, 1f), listOf(0.25f, 0.25f, 0.25f, 0.25f))
        )

        @JvmStatic
        fun partialSumsData(): Collection<Array<out Any>> = listOf(
            arrayOf(listOf(10f, 20f, 30f, 40f), listOf(0f, 10f, 30f, 60f, 100f)),
            arrayOf(listOf(1f, 1f, 1f, 1f), listOf(0f, 1f, 2f, 3f, 4f))
        )

        @JvmStatic
        fun withSizeData(): Collection<Array<out Any>> = listOf(
            arrayOf(listOf(1, 2, 3), 3, 17, listOf(1, 2, 3)),
            arrayOf(listOf(1, 2, 3), 2, 17, listOf(1, 2)),
            arrayOf(listOf(1, 2, 3), 4, 17, listOf(1, 2, 3, 3)),
            arrayOf(listOf(1, 2, 3), 5, 17, listOf(1, 2, 3, 3, 3)),
            arrayOf(emptyList<Int>(), 3, 17, listOf(17, 17, 17))
        )
    }
}
