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
