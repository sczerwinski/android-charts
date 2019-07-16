package it.czerwinski.android.charts.core

import org.junit.Assert.assertArrayEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class CollectionsNormalizeTest(
    private val input: List<Float>,
    private val normalizedList: List<Float>
) {

    @Test
    @Throws(Exception::class)
    fun shouldNormalizeList() {
        val result = input.normalize()

        assertArrayEquals(normalizedList.toFloatArray(), result.toFloatArray(), DELTA)
    }

    companion object TestData {

        @JvmStatic
        @Parameterized.Parameters(name = "norm({0}) = {1}")
        fun data(): Collection<Array<out Any>> = listOf(
            arrayOf(listOf(10f, 20f, 30f, 40f), listOf(0.1f, 0.2f, 0.3f, 0.4f)),
            arrayOf(listOf(1f, 1f, 1f, 1f), listOf(0.25f, 0.25f, 0.25f, 0.25f))
        )
    }
}
