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

package it.czerwinski.android.charts.piechart.adapters

import android.content.Context
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.spyk
import io.mockk.verify
import it.czerwinski.android.charts.piechart.R
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class FloatListPieChartAdapterTest {

    @MockK
    lateinit var context: Context

    @BeforeEach
    fun setUpContext() {
        every { context.getString(R.string.pie_chart_percent_format) } returns "%d%%"
    }

    @Test
    fun `When constructor, then create adapter with initial data`() {
        val initialData = listOf(1.0f, 2.0f, 3.0f)
        val adapter = FloatListPieChartAdapter(context, initialData)

        val result = adapter.data

        assertEquals(initialData, result)
    }

    @Test
    fun `When setData, then update data and notify observers`() {
        val initialData = listOf(1.0f, 2.0f, 3.0f)
        val updatedData = listOf(4.0f, 5.0f, 6.0f)
        val adapter = spyk(FloatListPieChartAdapter(context, initialData), recordPrivateCalls = true)
        justRun { adapter invokeNoArgs "notifyDataSetChanged" }

        adapter.data = updatedData
        val result = adapter.data

        assertEquals(updatedData, result)
        verify(exactly = 1) { adapter invokeNoArgs "notifyDataSetChanged" }
    }

    @Test
    fun `When size, then return size of data`() {
        val initialData = listOf(1.0f, 2.0f, 3.0f)
        val adapter = FloatListPieChartAdapter(context, initialData)

        val result = adapter.size

        assertEquals(3, result)
    }

    @Test
    fun `When sum, then return sum of data`() {
        val initialData = listOf(1.0f, 2.0f, 3.0f)
        val adapter = FloatListPieChartAdapter(context, initialData)

        val result = adapter.sum

        assertEquals(6.0f, result)
    }

    @Test
    fun `When get, then return value at the given index`() {
        val initialData = listOf(1.0f, 2.0f, 3.0f)
        val adapter = FloatListPieChartAdapter(context, initialData)

        val result = adapter[1]

        assertEquals(2.0f, result)
    }

    @Test
    fun `When getLabel, then return label at the given index`() {
        val initialData = listOf(1.0f, 2.0f, 3.0f)
        val adapter = FloatListPieChartAdapter(context, initialData)

        val result = adapter.getLabel(1)

        assertEquals("33%", result)
    }

    @Test
    fun `When iterator, then return iterator for data values`() {
        val initialData = listOf(1.0f, 2.0f, 3.0f)
        val adapter = FloatListPieChartAdapter(context, initialData)

        val result = adapter.iterator()

        assertEquals(listOf(1.0f, 2.0f, 3.0f), result.asSequence().toList())
    }

    @Test
    fun `When getLabels, then return iterable labels`() {
        val initialData = listOf(1.0f, 2.0f, 3.0f)
        val adapter = FloatListPieChartAdapter(context, initialData)

        val result = adapter.getLabels()

        assertEquals(listOf("16%", "33%", "50%"), result.toList())
    }
}
