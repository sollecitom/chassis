package org.sollecitom.chassis.identity.generator.ulid.java

import java.security.SecureRandom
import java.time.Clock
import java.util.*
import java.util.function.IntFunction
import java.util.function.LongFunction
import java.util.function.LongSupplier

internal class JavaUlidFactory private constructor(private val ulidFunction: LongFunction<JavaUlid>, timeMillisNow: LongSupplier? = null as LongSupplier?) {
    private val timeMillisNow // for tests
            : LongSupplier

    // ******************************
    // Constructors
    // ******************************
    constructor() : this(UlidFunction(IRandom.newInstance()))
    private constructor(ulidFunction: LongFunction<JavaUlid>, clock: Clock?) : this(ulidFunction, if (clock != null) LongSupplier { clock.millis() } else null)

    init {
        this.timeMillisNow = timeMillisNow ?: LongSupplier { Clock.systemUTC().millis() }
    }

    // ******************************
    // Public methods
    // ******************************
    @Synchronized
    fun create(): JavaUlid {
        return ulidFunction.apply(timeMillisNow.asLong)
    }

    @Synchronized
    fun create(time: Long): JavaUlid {
        return ulidFunction.apply(time)
    }

    // ******************************
    // Package-private inner classes
    // ******************************
    internal class UlidFunction(private val random: IRandom) : LongFunction<JavaUlid> {
        override fun apply(time: Long): JavaUlid {
            return if (random is ByteRandom) {
                JavaUlid(time, random.nextBytes(JavaUlid.RANDOM_BYTES))
            } else {
                val msb = time shl 16 or (random.nextLong() and 0xffffL)
                val lsb = random.nextLong()
                JavaUlid(msb, lsb)
            }
        }
    }

    internal class MonotonicFunction(private val random: IRandom, timeMillisNow: LongSupplier) : LongFunction<JavaUlid> {
        private var lastUlid: JavaUlid

        @JvmOverloads
        constructor(random: IRandom, clock: Clock = Clock.systemUTC()) : this(random, LongSupplier { clock.millis() })

        init {
            // initialize internal state
            lastUlid = JavaUlid(timeMillisNow.asLong, random.nextBytes(JavaUlid.RANDOM_BYTES))
        }

        @Synchronized
        override fun apply(time: Long): JavaUlid {
            val lastTime = lastUlid.time

            // Check if the current time is the same as the previous time or has moved
            // backwards after a small system clock adjustment or after a leap second.
            // Drift tolerance = (previous_time - 10s) < current_time <= previous_time
            if (time > lastTime - CLOCK_DRIFT_TOLERANCE && time <= lastTime) {
                lastUlid = lastUlid.increment()
            } else {
                if (random is ByteRandom) {
                    lastUlid = JavaUlid(time, random.nextBytes(JavaUlid.RANDOM_BYTES))
                } else {
                    val msb = time shl 16 or (random.nextLong() and 0xffffL)
                    val lsb = random.nextLong()
                    lastUlid = JavaUlid(msb, lsb)
                }
            }
            return JavaUlid(lastUlid)
        }

        companion object {
            // Used to preserve monotonicity when the system clock is
            // adjusted by NTP after a small clock drift or when the
            // system clock jumps back by 1 second due to leap second.
            protected const val CLOCK_DRIFT_TOLERANCE = 10000
        }
    }

    internal interface IRandom {
        fun nextLong(): Long
        fun nextBytes(length: Int): ByteArray?

        companion object {
            fun newInstance(): IRandom {
                return ByteRandom()
            }

            fun newInstance(random: Random?): IRandom {
                return if (random == null) {
                    ByteRandom()
                } else {
                    (random as? SecureRandom)?.let { ByteRandom(it) } ?: LongRandom(random)
                }
            }

            fun newInstance(randomFunction: LongSupplier?): IRandom {
                return LongRandom(randomFunction)
            }

            fun newInstance(randomFunction: IntFunction<ByteArray>?): IRandom {
                return ByteRandom(randomFunction)
            }
        }
    }

    internal class LongRandom @JvmOverloads constructor(randomFunction: LongSupplier? = newRandomFunction(null)) : IRandom {
        private val randomFunction: LongSupplier

        constructor(random: Random?) : this(newRandomFunction(random))

        init {
            this.randomFunction = randomFunction ?: newRandomFunction(null)
        }

        override fun nextLong(): Long {
            return randomFunction.asLong
        }

        override fun nextBytes(length: Int): ByteArray? {
            var shift = 0
            var random: Long = 0
            val bytes = ByteArray(length)
            for (i in 0 until length) {
                if (shift < java.lang.Byte.SIZE) {
                    shift = java.lang.Long.SIZE
                    random = randomFunction.asLong
                }
                shift -= java.lang.Byte.SIZE // 56, 48, 40...
                bytes[i] = (random ushr shift).toByte()
            }
            return bytes
        }

        companion object {
            fun newRandomFunction(random: Random?): LongSupplier {
                val entropy = random ?: SecureRandom()
                return LongSupplier { entropy.nextLong() }
            }
        }
    }

    internal class ByteRandom @JvmOverloads constructor(randomFunction: IntFunction<ByteArray>? = newRandomFunction(null)) : IRandom {
        private val randomFunction: IntFunction<ByteArray>

        constructor(random: Random?) : this(newRandomFunction(random))

        init {
            this.randomFunction = randomFunction ?: newRandomFunction(null)
        }

        override fun nextLong(): Long {
            var number: Long = 0
            val bytes = randomFunction.apply(java.lang.Long.BYTES)
            for (i in 0 until java.lang.Long.BYTES) {
                number = number shl 8 or (bytes[i].toInt() and 0xff).toLong()
            }
            return number
        }

        override fun nextBytes(length: Int): ByteArray? {
            return randomFunction.apply(length)
        }

        companion object {
            fun newRandomFunction(random: Random?): IntFunction<ByteArray> {
                val entropy = random ?: SecureRandom()
                return IntFunction<ByteArray> { length: Int ->
                    val bytes = ByteArray(length)
                    entropy.nextBytes(bytes)
                    bytes
                }
            }
        }
    }

    companion object {
        fun newInstance(): JavaUlidFactory {
            return JavaUlidFactory(UlidFunction(IRandom.newInstance()))
        }

        fun newInstance(random: Random?): JavaUlidFactory {
            return JavaUlidFactory(UlidFunction(IRandom.newInstance(random)))
        }

        fun newInstance(randomFunction: LongSupplier?): JavaUlidFactory {
            return JavaUlidFactory(UlidFunction(IRandom.newInstance(randomFunction)))
        }

        fun newInstance(randomFunction: IntFunction<ByteArray>?): JavaUlidFactory {
            return JavaUlidFactory(UlidFunction(IRandom.newInstance(randomFunction)))
        }

        fun newMonotonicInstance(): JavaUlidFactory {
            return JavaUlidFactory(MonotonicFunction(IRandom.newInstance()))
        }

        fun newMonotonicInstance(random: Random?): JavaUlidFactory {
            return JavaUlidFactory(MonotonicFunction(IRandom.newInstance(random)))
        }

        fun newMonotonicInstance(random: Random?, timeMillisNow: LongSupplier): JavaUlidFactory {
            return JavaUlidFactory(MonotonicFunction(IRandom.newInstance(random), timeMillisNow), timeMillisNow)
        }

        fun newMonotonicInstance(randomFunction: LongSupplier?): JavaUlidFactory {
            return JavaUlidFactory(MonotonicFunction(IRandom.newInstance(randomFunction)))
        }

        fun newMonotonicInstance(randomFunction: IntFunction<ByteArray>?): JavaUlidFactory {
            return JavaUlidFactory(MonotonicFunction(IRandom.newInstance(randomFunction)))
        }

        fun newMonotonicInstance(randomFunction: LongSupplier?, timeMillisNow: LongSupplier): JavaUlidFactory {
            return JavaUlidFactory(MonotonicFunction(IRandom.newInstance(randomFunction), timeMillisNow), timeMillisNow)
        }

        fun newMonotonicInstance(randomFunction: LongSupplier?, clock: Clock): JavaUlidFactory {
            return newMonotonicInstance(randomFunction) { clock.millis() }
        }

        fun newMonotonicInstance(randomFunction: IntFunction<ByteArray>?, timeMillisNow: LongSupplier): JavaUlidFactory {
            return JavaUlidFactory(MonotonicFunction(IRandom.newInstance(randomFunction), timeMillisNow), timeMillisNow)
        }

        fun newMonotonicInstance(randomFunction: IntFunction<ByteArray>?, clock: Clock): JavaUlidFactory {
            return newMonotonicInstance(randomFunction) { clock.millis() }
        }
    }
}