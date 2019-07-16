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
