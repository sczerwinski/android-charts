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

package it.czerwinski.android.charts.piechart

import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class PieChartExtensionsTest {

    @MockK
    lateinit var pieChart: PieChart

    @RelaxedMockK
    lateinit var listenerLambda: (view: PieChart, oldSelectionIndex: Int, newSelectionIndex: Int) -> Unit

    @MockK
    lateinit var adapter: PieChart.DataSetAdapter

    @RelaxedMockK
    lateinit var observerLambda: () -> Unit

    @Test
    fun `When addOnSelectionChangedListener, then add new selection changed listener from lambda`() {
        every { pieChart.addOnSelectionChangedListener(any()) } just Runs

        pieChart.addOnSelectionChangedListener(listenerLambda)

        val listenerSlot = slot<PieChart.OnSelectionChangedListener>()
        verify(exactly = 1) { pieChart.addOnSelectionChangedListener(capture(listenerSlot)) }

        listenerSlot.captured.onSelectionChangedListener(
            view = pieChart,
            oldSelectionIndex = 1,
            newSelectionIndex = 2
        )
        verify(exactly = 1) { listenerLambda(pieChart, 1, 2) }
    }

    @Test
    fun `When registerObserver, then add new data set observer from lambda`() {
        every { adapter.registerObserver(any()) } just Runs

        adapter.registerObserver(observerLambda)

        val observerSlot = slot<PieChart.DataSetObserver>()
        verify(exactly = 1) { adapter.registerObserver(capture(observerSlot)) }

        observerSlot.captured.onDataSetChanged()
        verify(exactly = 1) { observerLambda() }
    }
}
