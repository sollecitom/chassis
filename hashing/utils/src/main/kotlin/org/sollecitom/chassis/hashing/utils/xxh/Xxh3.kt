package org.sollecitom.chassis.hashing.utils.xxh

import net.openhft.hashing.LongHashFunction
import net.openhft.hashing.LongTupleHashFunction
import org.sollecitom.chassis.hashing.utils.Hash128Result
import org.sollecitom.chassis.hashing.utils.HashFunction
import org.sollecitom.chassis.hashing.utils.LongHashFunctionAdapter
import org.sollecitom.chassis.hashing.utils.LongTupleHashFunctionAdapter

object Xxh3 {

    val hash64: HashFunction<Long> by lazy { hash64(seed = 0L) }

    fun hash64(seed: Long): HashFunction<Long> = LongHashFunctionAdapter(delegate = xxh3(seed = seed))

    private fun xxh3(seed: Long): LongHashFunction = if (seed == 0L) LongHashFunction.xx3() else LongHashFunction.xx3(seed)
}