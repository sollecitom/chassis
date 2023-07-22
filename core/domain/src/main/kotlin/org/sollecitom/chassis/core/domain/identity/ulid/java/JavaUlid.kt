package org.sollecitom.chassis.core.domain.identity.ulid.java

import java.io.Serializable
import java.time.Instant
import java.util.*
import java.util.concurrent.ThreadLocalRandom

internal class JavaUlid : Serializable, Comparable<JavaUlid> {
    val mostSignificantBits // most significant bits
            : Long
    val leastSignificantBits // least significant bits
            : Long

    constructor(ulid: JavaUlid) {
        mostSignificantBits = ulid.mostSignificantBits
        leastSignificantBits = ulid.leastSignificantBits
    }

    constructor(mostSignificantBits: Long, leastSignificantBits: Long) {
        this.mostSignificantBits = mostSignificantBits
        this.leastSignificantBits = leastSignificantBits
    }

    constructor(time: Long, random: ByteArray?) {

        // The time component has 48 bits.
        require(time and -0x1000000000000L == 0L) {
            // ULID specification:
            // "Any attempt to decode or encode a ULID larger than this (time > 2^48-1)
            // should be rejected by all implementations, to prevent overflow bugs."
            "Invalid time value" // overflow or negative time!
        }
        // The random component has 80 bits (10 bytes).
        if (random == null || random.size != RANDOM_BYTES) {
            throw IllegalArgumentException("Invalid random bytes") // null or wrong length!
        }
        var long0: Long = 0
        var long1: Long = 0
        long0 = long0 or (time shl 16)
        long0 = long0 or ((random[0x0].toInt() and 0xff).toLong() shl 8)
        long0 = long0 or (random[0x1].toInt() and 0xff).toLong()
        long1 = long1 or ((random[0x2].toInt() and 0xff).toLong() shl 56)
        long1 = long1 or ((random[0x3].toInt() and 0xff).toLong() shl 48)
        long1 = long1 or ((random[0x4].toInt() and 0xff).toLong() shl 40)
        long1 = long1 or ((random[0x5].toInt() and 0xff).toLong() shl 32)
        long1 = long1 or ((random[0x6].toInt() and 0xff).toLong() shl 24)
        long1 = long1 or ((random[0x7].toInt() and 0xff).toLong() shl 16)
        long1 = long1 or ((random[0x8].toInt() and 0xff).toLong() shl 8)
        long1 = long1 or (random[0x9].toInt() and 0xff).toLong()
        mostSignificantBits = long0
        leastSignificantBits = long1
    }

    fun toUuid(): UUID {
        return UUID(mostSignificantBits, leastSignificantBits)
    }

    fun toBytes(): ByteArray {
        val bytes = ByteArray(ULID_BYTES)
        bytes[0x0] = (mostSignificantBits ushr 56).toByte()
        bytes[0x1] = (mostSignificantBits ushr 48).toByte()
        bytes[0x2] = (mostSignificantBits ushr 40).toByte()
        bytes[0x3] = (mostSignificantBits ushr 32).toByte()
        bytes[0x4] = (mostSignificantBits ushr 24).toByte()
        bytes[0x5] = (mostSignificantBits ushr 16).toByte()
        bytes[0x6] = (mostSignificantBits ushr 8).toByte()
        bytes[0x7] = mostSignificantBits.toByte()
        bytes[0x8] = (leastSignificantBits ushr 56).toByte()
        bytes[0x9] = (leastSignificantBits ushr 48).toByte()
        bytes[0xa] = (leastSignificantBits ushr 40).toByte()
        bytes[0xb] = (leastSignificantBits ushr 32).toByte()
        bytes[0xc] = (leastSignificantBits ushr 24).toByte()
        bytes[0xd] = (leastSignificantBits ushr 16).toByte()
        bytes[0xe] = (leastSignificantBits ushr 8).toByte()
        bytes[0xf] = leastSignificantBits.toByte()
        return bytes
    }

    override fun toString(): String {
        return toString(ALPHABET_UPPERCASE)
    }

    fun toLowerCase(): String {
        return toString(ALPHABET_LOWERCASE)
    }

    fun toRfc4122(): JavaUlid {

        // set the 4 most significant bits of the 7th byte to 0, 1, 0 and 0
        val msb4 = mostSignificantBits and -0xf001L or 0x0000000000004000L // RFC-4122 version 4
        // set the 2 most significant bits of the 9th byte to 1 and 0
        val lsb4 = leastSignificantBits and 0x3fffffffffffffffL or Long.MIN_VALUE // RFC-4122 variant 2
        Long.MIN_VALUE
        return JavaUlid(msb4, lsb4)
    }

    val instant: Instant
        get() = Instant.ofEpochMilli(time)
    val time: Long
        get() = mostSignificantBits ushr 16
    val random: ByteArray
        get() {
            val bytes = ByteArray(RANDOM_BYTES)
            bytes[0x0] = (mostSignificantBits ushr 8).toByte()
            bytes[0x1] = mostSignificantBits.toByte()
            bytes[0x2] = (leastSignificantBits ushr 56).toByte()
            bytes[0x3] = (leastSignificantBits ushr 48).toByte()
            bytes[0x4] = (leastSignificantBits ushr 40).toByte()
            bytes[0x5] = (leastSignificantBits ushr 32).toByte()
            bytes[0x6] = (leastSignificantBits ushr 24).toByte()
            bytes[0x7] = (leastSignificantBits ushr 16).toByte()
            bytes[0x8] = (leastSignificantBits ushr 8).toByte()
            bytes[0x9] = leastSignificantBits.toByte()
            return bytes
        }

    fun increment(): JavaUlid {
        var newMsb = mostSignificantBits
        val newLsb = leastSignificantBits + 1 // increment the LEAST significant bits
        if (newLsb == INCREMENT_OVERFLOW) {
            newMsb += 1 // increment the MOST significant bits
        }
        return JavaUlid(newMsb, newLsb)
    }

    override fun hashCode(): Int {
        val bits = mostSignificantBits xor leastSignificantBits
        return (bits xor (bits ushr 32)).toInt()
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other.javaClass != JavaUlid::class.java) return false
        val that = other as JavaUlid
        if (leastSignificantBits != that.leastSignificantBits) return false else if (mostSignificantBits != that.mostSignificantBits) return false
        return true
    }

    override fun compareTo(other: JavaUlid): Int {

        // used to compare as UNSIGNED longs
        val min = Long.MIN_VALUE
        val a = mostSignificantBits + min
        val b = other.mostSignificantBits + min
        if (a > b) return 1 else if (a < b) return -1
        val c = leastSignificantBits + min
        val d = other.leastSignificantBits + min
        if (c > d) return 1 else if (c < d) return -1
        return 0
    }

    fun toString(alphabet: CharArray): String {
        val chars = CharArray(ULID_CHARS)
        val time = mostSignificantBits ushr 16
        val random0 = mostSignificantBits and 0xffffL shl 24 or (leastSignificantBits ushr 40)
        val random1 = leastSignificantBits and 0xffffffffffL
        chars[0x00] = alphabet[((time ushr 45) and 31L).toInt()]
        chars[0x01] = alphabet[((time ushr 40) and 31L).toInt()]
        chars[0x02] = alphabet[((time ushr 35) and 31L).toInt()]
        chars[0x03] = alphabet[((time ushr 30) and 31L).toInt()]
        chars[0x04] = alphabet[((time ushr 25) and 31L).toInt()]
        chars[0x05] = alphabet[((time ushr 20) and 31L).toInt()]
        chars[0x06] = alphabet[((time ushr 15) and 31L).toInt()]
        chars[0x07] = alphabet[((time ushr 10) and 31L).toInt()]
        chars[0x08] = alphabet[((time ushr 5) and 31L).toInt()]
        chars[0x09] = alphabet[(time and 31L).toInt()]
        chars[0x0a] = alphabet[((random0 ushr 35) and 31L).toInt()]
        chars[0x0b] = alphabet[((random0 ushr 30) and 31L).toInt()]
        chars[0x0c] = alphabet[((random0 ushr 25) and 31L).toInt()]
        chars[0x0d] = alphabet[((random0 ushr 20) and 31L).toInt()]
        chars[0x0e] = alphabet[((random0 ushr 15) and 31L).toInt()]
        chars[0x0f] = alphabet[((random0 ushr 10) and 31L).toInt()]
        chars[0x10] = alphabet[((random0 ushr 5) and 31L).toInt()]
        chars[0x11] = alphabet[(random0 and 31L).toInt()]
        chars[0x12] = alphabet[((random1 ushr 35) and 31L).toInt()]
        chars[0x13] = alphabet[((random1 ushr 30) and 31L).toInt()]
        chars[0x14] = alphabet[((random1 ushr 25) and 31L).toInt()]
        chars[0x15] = alphabet[((random1 ushr 20) and 31L).toInt()]
        chars[0x16] = alphabet[((random1 ushr 15) and 31L).toInt()]
        chars[0x17] = alphabet[((random1 ushr 10) and 31L).toInt()]
        chars[0x18] = alphabet[((random1 ushr 5) and 31L).toInt()]
        chars[0x19] = alphabet[(random1 and 31L).toInt()]
        return String(chars)
    }

    companion object {
        private const val serialVersionUID = 2625269413446854731L
        const val ULID_CHARS = 26
        const val TIME_CHARS = 10
        const val RANDOM_CHARS = 16
        const val ULID_BYTES = 16
        const val TIME_BYTES = 6
        const val RANDOM_BYTES = 10
        val MIN = JavaUlid(0x0000000000000000L, 0x0000000000000000L)
        val MAX = JavaUlid(-0x1L, -0x1L)
        @JvmField
        val ALPHABET_VALUES = ByteArray(256)
        @JvmField
        val ALPHABET_UPPERCASE = "0123456789ABCDEFGHJKMNPQRSTVWXYZ".toCharArray()
        @JvmField
        val ALPHABET_LOWERCASE = "0123456789abcdefghjkmnpqrstvwxyz".toCharArray()

        init {

            // Initialize the alphabet map with -1
            Arrays.fill(ALPHABET_VALUES, (-1).toByte())

            // Map the alphabets chars to values
            for (i in ALPHABET_UPPERCASE.indices) {
                ALPHABET_VALUES[ALPHABET_UPPERCASE[i].code] = i.toByte()
            }
            for (i in ALPHABET_LOWERCASE.indices) {
                ALPHABET_VALUES[ALPHABET_LOWERCASE[i].code] = i.toByte()
            }

            // Upper case OIL
            ALPHABET_VALUES['O'.code] = 0x00
            ALPHABET_VALUES['I'.code] = 0x01
            ALPHABET_VALUES['L'.code] = 0x01

            // Lower case OIL
            ALPHABET_VALUES['o'.code] = 0x00
            ALPHABET_VALUES['i'.code] = 0x01
            ALPHABET_VALUES['l'.code] = 0x01
        }

        // 0xffffffffffffffffL + 1 = 0x0000000000000000L
        private const val INCREMENT_OVERFLOW = 0x0000000000000000L
        fun fast(): JavaUlid {
            val time = System.currentTimeMillis()
            val random = ThreadLocalRandom.current()
            return JavaUlid(time shl 16 or (random.nextLong() and 0xffffL), random.nextLong())
        }

        fun min(time: Long): JavaUlid {
            return JavaUlid(time shl 16 or 0x0000L, 0x0000000000000000L)
        }

        fun max(time: Long): JavaUlid {
            return JavaUlid(time shl 16 or 0xffffL, -0x1L)
        }

        fun from(uuid: UUID): JavaUlid {
            return JavaUlid(uuid.mostSignificantBits, uuid.leastSignificantBits)
        }

        fun from(bytes: ByteArray?): JavaUlid {
            if (bytes == null || bytes.size != ULID_BYTES) {
                throw IllegalArgumentException("Invalid ULID bytes") // null or wrong length!
            }
            var msb: Long = 0
            var lsb: Long = 0
            msb = msb or (bytes[0x0].toLong() and 0xffL shl 56)
            msb = msb or (bytes[0x1].toLong() and 0xffL shl 48)
            msb = msb or (bytes[0x2].toLong() and 0xffL shl 40)
            msb = msb or (bytes[0x3].toLong() and 0xffL shl 32)
            msb = msb or (bytes[0x4].toLong() and 0xffL shl 24)
            msb = msb or (bytes[0x5].toLong() and 0xffL shl 16)
            msb = msb or (bytes[0x6].toLong() and 0xffL shl 8)
            msb = msb or (bytes[0x7].toLong() and 0xffL)
            lsb = lsb or (bytes[0x8].toLong() and 0xffL shl 56)
            lsb = lsb or (bytes[0x9].toLong() and 0xffL shl 48)
            lsb = lsb or (bytes[0xa].toLong() and 0xffL shl 40)
            lsb = lsb or (bytes[0xb].toLong() and 0xffL shl 32)
            lsb = lsb or (bytes[0xc].toLong() and 0xffL shl 24)
            lsb = lsb or (bytes[0xd].toLong() and 0xffL shl 16)
            lsb = lsb or (bytes[0xe].toLong() and 0xffL shl 8)
            lsb = lsb or (bytes[0xf].toLong() and 0xffL)
            return JavaUlid(msb, lsb)
        }

        fun from(string: String?): JavaUlid {
            val chars = toCharArray(string)
            var time: Long = 0
            var random0: Long = 0
            var random1: Long = 0
            time = time or (ALPHABET_VALUES[chars!![0x00].code].toLong() shl 45)
            time = time or (ALPHABET_VALUES[chars[0x01].code].toLong() shl 40)
            time = time or (ALPHABET_VALUES[chars[0x02].code].toLong() shl 35)
            time = time or (ALPHABET_VALUES[chars[0x03].code].toLong() shl 30)
            time = time or (ALPHABET_VALUES[chars[0x04].code].toLong() shl 25)
            time = time or (ALPHABET_VALUES[chars[0x05].code].toLong() shl 20)
            time = time or (ALPHABET_VALUES[chars[0x06].code].toLong() shl 15)
            time = time or (ALPHABET_VALUES[chars[0x07].code].toLong() shl 10)
            time = time or (ALPHABET_VALUES[chars[0x08].code].toLong() shl 5)
            time = time or ALPHABET_VALUES[chars[0x09].code].toLong()
            random0 = random0 or (ALPHABET_VALUES[chars[0x0a].code].toLong() shl 35)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0b].code].toLong() shl 30)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0c].code].toLong() shl 25)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0d].code].toLong() shl 20)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0e].code].toLong() shl 15)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0f].code].toLong() shl 10)
            random0 = random0 or (ALPHABET_VALUES[chars[0x10].code].toLong() shl 5)
            random0 = random0 or ALPHABET_VALUES[chars[0x11].code].toLong()
            random1 = random1 or (ALPHABET_VALUES[chars[0x12].code].toLong() shl 35)
            random1 = random1 or (ALPHABET_VALUES[chars[0x13].code].toLong() shl 30)
            random1 = random1 or (ALPHABET_VALUES[chars[0x14].code].toLong() shl 25)
            random1 = random1 or (ALPHABET_VALUES[chars[0x15].code].toLong() shl 20)
            random1 = random1 or (ALPHABET_VALUES[chars[0x16].code].toLong() shl 15)
            random1 = random1 or (ALPHABET_VALUES[chars[0x17].code].toLong() shl 10)
            random1 = random1 or (ALPHABET_VALUES[chars[0x18].code].toLong() shl 5)
            random1 = random1 or ALPHABET_VALUES[chars[0x19].code].toLong()
            val msb = time shl 16 or (random0 ushr 24)
            val lsb = random0 shl 40 or (random1 and 0xffffffffffL)
            return JavaUlid(msb, lsb)
        }

        fun getInstant(string: String?): Instant {
            return Instant.ofEpochMilli(getTime(string))
        }

        @JvmStatic
        fun getTime(string: String?): Long {
            val chars = toCharArray(string)
            var time: Long = 0
            time = time or (ALPHABET_VALUES[chars!![0x00].code].toLong() shl 45)
            time = time or (ALPHABET_VALUES[chars[0x01].code].toLong() shl 40)
            time = time or (ALPHABET_VALUES[chars[0x02].code].toLong() shl 35)
            time = time or (ALPHABET_VALUES[chars[0x03].code].toLong() shl 30)
            time = time or (ALPHABET_VALUES[chars[0x04].code].toLong() shl 25)
            time = time or (ALPHABET_VALUES[chars[0x05].code].toLong() shl 20)
            time = time or (ALPHABET_VALUES[chars[0x06].code].toLong() shl 15)
            time = time or (ALPHABET_VALUES[chars[0x07].code].toLong() shl 10)
            time = time or (ALPHABET_VALUES[chars[0x08].code].toLong() shl 5)
            time = time or ALPHABET_VALUES[chars[0x09].code].toLong()
            return time
        }

        fun getRandom(string: String?): ByteArray {
            val chars = toCharArray(string)
            var random0: Long = 0
            var random1: Long = 0
            random0 = random0 or (ALPHABET_VALUES[chars!![0x0a].code].toLong() shl 35)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0b].code].toLong() shl 30)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0c].code].toLong() shl 25)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0d].code].toLong() shl 20)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0e].code].toLong() shl 15)
            random0 = random0 or (ALPHABET_VALUES[chars[0x0f].code].toLong() shl 10)
            random0 = random0 or (ALPHABET_VALUES[chars[0x10].code].toLong() shl 5)
            random0 = random0 or ALPHABET_VALUES[chars[0x11].code].toLong()
            random1 = random1 or (ALPHABET_VALUES[chars[0x12].code].toLong() shl 35)
            random1 = random1 or (ALPHABET_VALUES[chars[0x13].code].toLong() shl 30)
            random1 = random1 or (ALPHABET_VALUES[chars[0x14].code].toLong() shl 25)
            random1 = random1 or (ALPHABET_VALUES[chars[0x15].code].toLong() shl 20)
            random1 = random1 or (ALPHABET_VALUES[chars[0x16].code].toLong() shl 15)
            random1 = random1 or (ALPHABET_VALUES[chars[0x17].code].toLong() shl 10)
            random1 = random1 or (ALPHABET_VALUES[chars[0x18].code].toLong() shl 5)
            random1 = random1 or ALPHABET_VALUES[chars[0x19].code].toLong()
            val bytes = ByteArray(RANDOM_BYTES)
            bytes[0x0] = (random0 ushr 32).toByte()
            bytes[0x1] = (random0 ushr 24).toByte()
            bytes[0x2] = (random0 ushr 16).toByte()
            bytes[0x3] = (random0 ushr 8).toByte()
            bytes[0x4] = random0.toByte()
            bytes[0x5] = (random1 ushr 32).toByte()
            bytes[0x6] = (random1 ushr 24).toByte()
            bytes[0x7] = (random1 ushr 16).toByte()
            bytes[0x8] = (random1 ushr 8).toByte()
            bytes[0x9] = random1.toByte()
            return bytes
        }

        fun isValid(string: String?): Boolean {
            return string != null && isValidCharArray(string.toCharArray())
        }

        @JvmStatic
        fun toCharArray(string: String?): CharArray? {
            val chars = string?.toCharArray()
            if (!isValidCharArray(chars)) {
                throw IllegalArgumentException(String.format("Invalid ULID: \"%s\"", string))
            }
            return chars
        }

        @JvmStatic
        fun isValidCharArray(chars: CharArray?): Boolean {
            if (chars == null || chars.size != ULID_CHARS) {
                return false // null or wrong size!
            }

            // The time component has 48 bits.
            // The base32 encoded time component has 50 bits.
            // The time component cannot be greater than than 2^48-1.
            // So the 2 first bits of the base32 decoded time component must be ZERO.
            // As a consequence, the 1st char of the input string must be between 0 and 7.
            if (ALPHABET_VALUES[chars[0].code].toInt() and 24 != 0) {
                // ULID specification:
                // "Any attempt to decode or encode a ULID larger than this (time > 2^48-1)
                // should be rejected by all implementations, to prevent overflow bugs."
                return false // time overflow!
            }
            for (i in chars.indices) {
                if (ALPHABET_VALUES[chars[i].code].toInt() == -1) {
                    return false // invalid character!
                }
            }
            return true // It seems to be OK.
        }
    }
}