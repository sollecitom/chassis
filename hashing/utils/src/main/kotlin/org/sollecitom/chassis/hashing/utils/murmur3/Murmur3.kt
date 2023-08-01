package org.sollecitom.chassis.hashing.utils.murmur3

import org.apache.commons.codec.digest.MurmurHash3

object Murmur3 {

    val hash32: Murmur3HashFunction<Int> = object : Murmur3HashFunction<Int> {
        override fun invoke(bytes: ByteArray, offset: Int, length: Int, seed: Int) = MurmurHash3.hash32x86(bytes, offset, length, seed)
    }

    val hash64: Murmur3HashFunction<Long> = object : Murmur3HashFunction<Long> {
        override fun invoke(bytes: ByteArray, offset: Int, length: Int, seed: Int) = hash128(bytes, offset, length, seed).firstHalf
    }

    val hash128: Murmur3HashFunction<Hash128Result> = object : Murmur3HashFunction<Hash128Result> {
        override fun invoke(bytes: ByteArray, offset: Int, length: Int, seed: Int) = MurmurHash3.hash128x64(bytes, offset, length, seed).let(Hash128Result::create)
    }
}