package org.sollecitom.chassis.hashing.utils.murmur3

interface Murmur3HashFunction<RESULT : Any> {

    operator fun invoke(bytes: ByteArray, offset: Int = 0, length: Int = bytes.size, seed: Int = 0): RESULT
}

fun <VALUE : Any, RESULT : Any> Murmur3HashFunction<RESULT>.invoke(value: VALUE, toByteArray: VALUE.() -> ByteArray, seed: Int = 0) = invoke(bytes = value.toByteArray(), seed = seed)