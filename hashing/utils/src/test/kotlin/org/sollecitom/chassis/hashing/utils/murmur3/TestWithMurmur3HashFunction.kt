package org.sollecitom.chassis.hashing.utils.murmur3

import assertk.Assert
import assertk.assertions.isEqualTo

interface TestWithMurmur3HashFunction<RESULT : Any> {

    val hash: Murmur3HashFunction<RESULT>

    fun Assert<RESULT>.matches(expected: RESULT) = isEqualTo(expected)
}