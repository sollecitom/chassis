package com.element.dpg.libs.chassis.hashing.utils.xxh

import com.element.dpg.libs.chassis.hashing.utils.Hash128Result
import com.element.dpg.libs.chassis.hashing.utils.HashFunction
import com.element.dpg.libs.chassis.hashing.utils.LongHashFunctionAdapter
import com.element.dpg.libs.chassis.hashing.utils.LongTupleHashFunctionAdapter
import net.openhft.hashing.LongHashFunction
import net.openhft.hashing.LongTupleHashFunction

object Xxh {

    val hash64: HashFunction<Long> by lazy { hash64(seed = 0L) }

    val hash128: HashFunction<Hash128Result> by lazy { hash128(seed = 0L) }

    fun hash64(seed: Long): HashFunction<Long> = LongHashFunctionAdapter(delegate = xxh_64(seed = seed))

    fun hash128(seed: Long): HashFunction<Hash128Result> = LongTupleHashFunctionAdapter(delegate = xxh_128(seed = seed))

    private fun xxh_64(seed: Long): LongHashFunction = if (seed == 0L) LongHashFunction.xx() else LongHashFunction.xx(seed)
    private fun xxh_128(seed: Long): LongTupleHashFunction = if (seed == 0L) LongTupleHashFunction.xx128() else LongTupleHashFunction.xx128(seed)
}