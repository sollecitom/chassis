package org.sollecitom.chassis.kotlin.extensions.bytes

import org.sollecitom.chassis.kotlin.extensions.number.roundToCeil
import java.nio.ByteBuffer
import kotlin.math.log2

fun ByteBuffer.toByteArray(): ByteArray {

    val byteArray = ByteArray(capacity())
    get(byteArray)
    return byteArray
}

val Int.requiredBits: Int get() = log2(toDouble()).roundToCeil()