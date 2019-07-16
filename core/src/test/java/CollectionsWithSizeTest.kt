package it.czerwinski.android.charts.core

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class CollectionsWithSizeTest(
    private val input: List<Int>,
    private val size: Int,
    private val valueIfEmpty: Int,
    private val resizedList: List<Int>
) {

    @Test
    @Throws(Exception::class)
    fun shouldChangeSize() {
        val result = input.withSize(size, valueIfEmpty)

        assertEquals(resizedList, result)
    }

    companion object TestData {

        @JvmStatic
        @Parameterized.Parameters(name = "{0}.withSize({1}, ifEmpty = {2}) = {3}")
        fun data(): Collection<Array<out Any>> = listOf(
            arrayOf(listOf(1, 2, 3), 3, 17, listOf(1, 2, 3)),
            arrayOf(listOf(1, 2, 3), 2, 17, listOf(1, 2)),
            arrayOf(listOf(1, 2, 3), 4, 17, listOf(1, 2, 3, 3)),
            arrayOf(listOf(1, 2, 3), 5, 17, listOf(1, 2, 3, 3, 3)),
            arrayOf(emptyList<Int>(), 3, 17, listOf(17, 17, 17))
        )
    }
}
