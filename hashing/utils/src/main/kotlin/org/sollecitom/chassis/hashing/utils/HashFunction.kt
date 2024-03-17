package org.sollecitom.chassis.hashing.utils

interface HashFunction<RESULT : Any> {

    operator fun invoke(bytes: ByteArray, offset: Int = 0, length: Int = bytes.size): RESULT
}

operator fun <VALUE : Any, RESULT : Any> HashFunction<RESULT>.invoke(value: VALUE, toByteArray: VALUE.() -> ByteArray) = invoke(bytes = value.toByteArray())