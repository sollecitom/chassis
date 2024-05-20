package com.element.dpg.libs.chassis.hashing.utils.murmur3

import net.openhft.hashing.LongHashFunction
import net.openhft.hashing.LongTupleHashFunction
import com.element.dpg.libs.chassis.hashing.utils.Hash128Result
import com.element.dpg.libs.chassis.hashing.utils.HashFunction
import com.element.dpg.libs.chassis.hashing.utils.LongHashFunctionAdapter
import com.element.dpg.libs.chassis.hashing.utils.LongTupleHashFunctionAdapter

object Murmur3 {

    val hash64: HashFunction<Long> by lazy { hash64(seed = 0L) }

    val hash128: HashFunction<Hash128Result> by lazy { hash128(seed = 0L) }

    fun hash64(seed: Long): HashFunction<Long> = LongHashFunctionAdapter(delegate = murmur3_64(seed = seed))

    fun hash128(seed: Long): HashFunction<Hash128Result> = LongTupleHashFunctionAdapter(delegate = murmur3_128(seed = seed))

    private fun murmur3_64(seed: Long): LongHashFunction = if (seed == 0L) LongHashFunction.murmur_3() else LongHashFunction.murmur_3(seed)
    private fun murmur3_128(seed: Long): LongTupleHashFunction = if (seed == 0L) LongTupleHashFunction.murmur_3() else LongTupleHashFunction.murmur_3(seed)
}