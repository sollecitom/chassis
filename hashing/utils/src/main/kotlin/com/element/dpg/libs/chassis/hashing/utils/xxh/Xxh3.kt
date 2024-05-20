package com.element.dpg.libs.chassis.hashing.utils.xxh

import com.element.dpg.libs.chassis.hashing.utils.HashFunction
import com.element.dpg.libs.chassis.hashing.utils.LongHashFunctionAdapter
import net.openhft.hashing.LongHashFunction

object Xxh3 {

    val hash64: HashFunction<Long> by lazy { hash64(seed = 0L) }

    fun hash64(seed: Long): HashFunction<Long> = LongHashFunctionAdapter(delegate = xxh3(seed = seed))

    private fun xxh3(seed: Long): LongHashFunction = if (seed == 0L) LongHashFunction.xx3() else LongHashFunction.xx3(seed)
}