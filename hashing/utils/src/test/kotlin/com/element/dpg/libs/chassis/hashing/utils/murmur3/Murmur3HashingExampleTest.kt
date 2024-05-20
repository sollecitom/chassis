package com.element.dpg.libs.chassis.hashing.utils.murmur3

import assertk.Assert
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.hashing.utils.Hash128Result
import com.element.dpg.libs.chassis.hashing.utils.HashFunctionTestSpecification

@TestInstance(PER_CLASS)
private class Murmur3HashingExampleTest {

    @Nested
    inner class Hash64 : HashFunctionTestSpecification<Long> {

        override fun hashFunction(seed: Long) = Murmur3.hash64(seed)
        override val digest = "a message"
        override val expectedHash = -3060066523902502006L
    }

    @Nested
    inner class Hash128 : HashFunctionTestSpecification<Hash128Result> {

        override fun hashFunction(seed: Long) = Murmur3.hash128(seed)
        override val digest = "a message"
        override val expectedHash = Hash128Result.create(-3060066523902502006L, 2187737632784832728L)

        override fun Assert<Hash128Result>.matches(expected: Hash128Result) = given { actual ->

            assertThat(actual.firstHalf).isEqualTo(expected.firstHalf)
            assertThat(actual.secondHalf).isEqualTo(expected.secondHalf)
        }
    }
}