package org.sollecitom.chassis.kotlin.extensions.bytes

import java.nio.ByteBuffer

fun ByteBuffer.toByteArray(): ByteArray {

    val byteArray = ByteArray(capacity())
    get(byteArray)
    return byteArray
}