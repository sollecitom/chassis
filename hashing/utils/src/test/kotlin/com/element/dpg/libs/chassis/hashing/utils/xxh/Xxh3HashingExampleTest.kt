package com.element.dpg.libs.chassis.hashing.utils.xxh

import com.element.dpg.libs.chassis.hashing.utils.HashFunctionTestSpecification
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class Xxh3HashingExampleTest {

    @Nested
    inner class Hash64 : HashFunctionTestSpecification<Long> {

        override fun hashFunction(seed: Long) = Xxh3.hash64(seed)
        override val digest = "a message"
        override val expectedHash = -4177236279915762211L
    }
}