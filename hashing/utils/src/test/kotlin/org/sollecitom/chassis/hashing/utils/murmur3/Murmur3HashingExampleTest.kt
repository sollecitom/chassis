package org.sollecitom.chassis.hashing.utils.murmur3

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class Murmur3HashingExampleTest {

    @Nested
    inner class Hash32 : HashFunctionTestSpecification<Int> {

        override val hash get() = Murmur3.hash32

        @Test
        fun `the hash produced matches the one Murmur3 produces`() {

            val message = "a message"
            val bytes = message.toByteArray()
            val expectedMurmur3Hash = 728939086

            val hash = hash(bytes)

            assertThat(hash).matches(expectedMurmur3Hash)
        }
    }

    @Nested
    inner class Hash64 : HashFunctionTestSpecification<Long> {

        override val hash get() = Murmur3.hash64

        @Test
        fun `the hash produced matches the one Murmur3 produces`() {

            val message = "a message"
            val bytes = message.toByteArray()
            val expectedMurmur3Hash = -3060066523902502006L

            val hash = hash(bytes)

            assertThat(hash).matches(expectedMurmur3Hash)
        }
    }

    @Nested
    inner class Hash128 : HashFunctionTestSpecification<Hash128Result> {

        override val hash get() = Murmur3.hash128

        override fun Assert<Hash128Result>.matches(expected: Hash128Result) = given { actual ->

            assertThat(actual.firstHalf).isEqualTo(expected.firstHalf)
            assertThat(actual.secondHalf).isEqualTo(expected.secondHalf)
        }

        @Test
        fun `the hash produced matches the one Murmur3 produces`() {

            val message = "a message"
            val bytes = message.toByteArray()
            val expectedMurmur3HashParts = Hash128Result.create(-3060066523902502006L, 2187737632784832728L)

            val hash = hash(bytes)

            assertThat(hash).matches(expectedMurmur3HashParts)
        }
    }
}