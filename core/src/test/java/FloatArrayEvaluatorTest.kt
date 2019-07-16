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

import org.junit.Assert.assertArrayEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class FloatArrayEvaluatorTest(
    private val fraction: Float,
    startValues: List<Float>,
    endValues: List<Float>,
    expectedResult: List<Float>
) {

    private val startArray: FloatArray = startValues.toFloatArray()
    private val endArray: FloatArray = endValues.toFloatArray()
    private val expectedResultArray: FloatArray = expectedResult.toFloatArray()

    @Test
    @Throws(Exception::class)
    fun evaluatorShouldReturnCorrectResult() {
        val evaluator = FloatArrayEvaluator()

        val resultArray = evaluator.evaluate(fraction, startArray, endArray)

        assertArrayEquals(expectedResultArray, resultArray, DELTA)
    }

    companion object TestData {

        @JvmStatic
        @Parameterized.Parameters(name = "eval({0}, {1}, {2}) = {3}")
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
