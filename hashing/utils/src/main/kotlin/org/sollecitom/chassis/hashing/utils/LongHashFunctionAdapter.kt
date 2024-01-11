package org.sollecitom.chassis.hashing.utils

import net.openhft.hashing.LongHashFunction
import org.sollecitom.chassis.hashing.utils.HashFunction

@JvmInline
internal value class LongHashFunctionAdapter(private val delegate: LongHashFunction) : HashFunction<Long> {

    override fun invoke(bytes: ByteArray, offset: Int, length: Int) = delegate.hashBytes(bytes, offset, length)
}