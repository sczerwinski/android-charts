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
class CollectionsPartialSumsTest(
    private val input: List<Float>,
    private val normalizedList: List<Float>
) {

    @Test
    @Throws(Exception::class)
    fun shouldCalculatePartialSums() {
        val result = input.partialSums()

        assertArrayEquals(normalizedList.toFloatArray(), result.toFloatArray(), DELTA)
    }

    companion object TestData {

        @JvmStatic
        @Parameterized.Parameters(name = "sums({0}) = {1}")
        fun data(): Collection<Array<out Any>> = listOf(
            arrayOf(listOf(10f, 20f, 30f, 40f), listOf(0f, 10f, 30f, 60f, 100f)),
            arrayOf(listOf(1f, 1f, 1f, 1f), listOf(0f, 1f, 2f, 3f, 4f))
        )
    }
}
