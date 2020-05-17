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

@file:JvmName(name = "Collections")

package it.czerwinski.android.charts.core

/**
 * Returns a list containing elements whose sum is equal to 1.
 * The ratios of all list elements are preserved from the original list.
 *
 * If the sum of all elements is 0, returns the original list.
 *
 * @receiver Original list.
 * @return Normalized list.
 */
fun List<Float>.normalize(): List<Float> {
    val sum = sum()
    return if (sum > 0f) map { it / sum } else this
}

/**
 * Calculates sums of all prior elements in the original iterable object, starting with 0.
 *
 * @receiver Original iterable.
 * @return A list of partial sums.
 */
fun Iterable<Float>.partialSums(): List<Float> =
    fold(listOf(0f)) { sums, next ->
        sums + (sums.last() + next)
    }

/**
 * Creates a list with the specified size.
 *
 * If the new list is longer than the original iterable object, the remaining elements are equal
 * to the last element of the original iterable object.
 *
 * If the original iterable object is empty, the new list will be filled with `valueIfEmpty`.
 *
 * @receiver Original iterable.
 * @param size Size of the new list.
 * @param valueIfEmpty Value to be used for each element of the new list
 *        if the original iterable is empty.
 * @return A list with the specified size.
 */
fun <T : Any> Iterable<T>.withSize(size: Int, valueIfEmpty: T): List<T> =
    (asSequence() + generateSequence { lastOrNull() ?: valueIfEmpty })
        .take(size)
        .toList()
