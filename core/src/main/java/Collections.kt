package it.czerwinski.android.charts.common

fun List<Float>.normalize(): List<Float> {
    val sum = sum()
    return if (sum > 0f) map { it / sum } else this
}

fun Iterable<Float>.partialSums(): List<Float> =
    fold(listOf(0f)) { sums, next ->
        sums + (sums.last() + next)
    }

fun <T : Any> Iterable<T>.withSize(size: Int, valueIfEmpty: T): List<T> =
    (asSequence() + generateSequence { lastOrNull() ?: valueIfEmpty })
        .take(size)
        .toList()
