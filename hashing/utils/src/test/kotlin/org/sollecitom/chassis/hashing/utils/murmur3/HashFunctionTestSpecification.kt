package org.sollecitom.chassis.hashing.utils.murmur3

import assertk.assertThat
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.kotlin.extensions.text.strings
import kotlin.random.Random

interface HashFunctionTestSpecification<RESULT : Any> : TestWithMurmur3HashFunction<RESULT> {

    @Test
    fun `hashing the same value with the same seed produces the same hash`() {

        Random.strings(wordLengths = 4..10).onEach { println("Testing for 'word': $it") }.take(100).forEach { word ->

            val bytes = word.toByteArray()
            val sameBytesAgain = word.toByteArray()

            val hash = hash(bytes = bytes)
            val secondHash = hash(bytes = sameBytesAgain)

            assertThat(hash).matches(secondHash)
        }
    }
}