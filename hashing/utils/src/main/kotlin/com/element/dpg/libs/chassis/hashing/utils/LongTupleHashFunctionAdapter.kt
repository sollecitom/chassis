package com.element.dpg.libs.chassis.hashing.utils

import net.openhft.hashing.LongTupleHashFunction

@JvmInline
internal value class LongTupleHashFunctionAdapter(private val delegate: LongTupleHashFunction) : HashFunction<Hash128Result> {

    override fun invoke(bytes: ByteArray, offset: Int, length: Int) = delegate.hashBytes(bytes, offset, length).let(::Hash128Result)
}