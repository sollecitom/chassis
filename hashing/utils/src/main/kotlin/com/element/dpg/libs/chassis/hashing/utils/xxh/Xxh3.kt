package com.element.dpg.libs.chassis.hashing.utils.xxh

import net.openhft.hashing.LongHashFunction
import com.element.dpg.libs.chassis.hashing.utils.HashFunction
import com.element.dpg.libs.chassis.hashing.utils.LongHashFunctionAdapter

object Xxh3 {

    val hash64: HashFunction<Long> by lazy { hash64(seed = 0L) }

    fun hash64(seed: Long): HashFunction<Long> = LongHashFunctionAdapter(delegate = xxh3(seed = seed))

    private fun xxh3(seed: Long): LongHashFunction = if (seed == 0L) LongHashFunction.xx3() else LongHashFunction.xx3(seed)
}