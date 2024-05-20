package com.element.dpg.libs.chassis.hashing.utils

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import com.element.dpg.libs.chassis.kotlin.extensions.text.string
import com.element.dpg.libs.chassis.kotlin.extensions.text.strings
import org.junit.jupiter.api.Test
import kotlin.random.Random

interface HashFunctionTestSpecification<RESULT : Any> {

    val digest: String
    val expectedHash: RESULT

    fun hashFunction(seed: Long = 0L): HashFunction<RESULT>

    fun Assert<RESULT>.matches(expected: RESULT) = isEqualTo(expected)

    @Test
    fun `hashing the same value with the same seed produces the same hash`() {

        val seed = Random.nextLong()
        val hashFunction = hashFunction(seed = seed)
        Random.strings(wordLengths = 4..10).onEach { println("Testing for 'word': $it") }.take(100).forEach { word ->

            val bytes = word.toByteArray()
            val sameBytesAgain = word.toByteArray()

            val firstHash = hashFunction(bytes = bytes)
            val secondHash = hashFunction(bytes = sameBytesAgain)

            assertThat(firstHash).matches(secondHash)
        }
    }

    @Test
    fun `hashing the same value with a different seed produces a different hash`() {

        val firstSeed = Random.nextLong()
        val secondSeed = firstSeed + Random.nextLong()
        val firstHashFunction = hashFunction(seed = firstSeed)
        val secondHashFunction = hashFunction(seed = secondSeed)
        val digest = Random.string(wordLengths = 4..10)

        val firstHash = firstHashFunction(digest.toByteArray())
        val secondHash = secondHashFunction(digest.toByteArray())

        assertThat(firstHash).isNotEqualTo(secondHash)
    }

    @Test
    fun `the hash produced matches the one the algorithm produces`() {

        val hashFunction = hashFunction()
        val bytes = digest.toByteArray()

        val hash = hashFunction(bytes)

        assertThat(hash).matches(expectedHash)
    }
}