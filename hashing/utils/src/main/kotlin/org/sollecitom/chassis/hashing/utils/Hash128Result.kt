package org.sollecitom.chassis.hashing.utils

@JvmInline
value class Hash128Result internal constructor(private val parts: LongArray) {

    val firstHalf: Long get() = parts[0]
    val secondHalf: Long get() = parts[1]

    operator fun component1(): Long = firstHalf

    operator fun component2(): Long = secondHalf

    companion object {

        fun create(parts: LongArray): Hash128Result {
            require(parts.size == 2) { "The result of hashing something with 128 bits only takes 2 Longs" }
            return Hash128Result(parts)
        }

        fun create(firstHalf: Long, secondHalf: Long): Hash128Result = create(longArrayOf(firstHalf, secondHalf))
    }
}