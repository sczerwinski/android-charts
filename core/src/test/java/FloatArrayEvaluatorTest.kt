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
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class FloatArrayEvaluatorTest {

    @ParameterizedTest(name = "evaluate({0}, {1}, {2}) = {3}")
    @MethodSource("data")
    fun `When evaluate, then return an array with values at the specified fraction between original arrays`(
        fraction: Float,
        startValues: List<Float>,
        endValues: List<Float>,
        expectedResult: List<Float>
    ) {
        val startArray: FloatArray = startValues.toFloatArray()
        val endArray: FloatArray = endValues.toFloatArray()
        val expectedResultArray: FloatArray = expectedResult.toFloatArray()

        val evaluator = FloatArrayEvaluator()

        val resultArray = evaluator.evaluate(fraction, startArray, endArray)

        assertArrayEquals(expectedResultArray, resultArray, DELTA)
    }

    companion object TestData {

        @JvmStatic
        fun data(): Collection<Array<out Any>> = listOf(
            arrayOf(0f, listOf(10f, 15f, 20f), listOf(20f, 15f, 10f), listOf(10f, 15f, 20f)),
            arrayOf(1f, listOf(10f, 15f, 20f), listOf(20f, 15f, 10f), listOf(20f, 15f, 10f)),
            arrayOf(.5f, listOf(10f, 15f, 20f), listOf(20f, 15f, 10f), listOf(15f, 15f, 15f)),
            arrayOf(.25f, listOf(10f, 15f, 20f), listOf(20f, 15f, 10f), listOf(12.5f, 15f, 17.5f)),
            arrayOf(2f, listOf(10f, 15f, 20f), listOf(20f, 15f, 10f), listOf(30f, 15f, 0f)),
            arrayOf(-1f, listOf(10f, 15f, 20f), listOf(20f, 15f, 10f), listOf(0f, 15f, 30f))
        )
    }
}
