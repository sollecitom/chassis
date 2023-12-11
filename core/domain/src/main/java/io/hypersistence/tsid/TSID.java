
package io.hypersistence.tsid;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Clock;
import java.time.Instant;
import java.util.Random;
import java.util.SplittableRandom;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

// TODO remove after https://github.com/vladmihalcea/hypersistence-tsid/pull/10 will have been merged and released

public final class TSID implements Serializable, Comparable<TSID> {

    private static final long serialVersionUID = -5446820982139116297L;

    private final long number;

    public static final int TSID_BYTES = 8;

    public static final int TSID_CHARS = 13;

    public static final long TSID_EPOCH = Instant.parse("2020-01-01T00:00:00.000Z").toEpochMilli();

    static final int RANDOM_BITS = 22;
    static final int RANDOM_MASK = 0x003fffff;

    private static final char[] ALPHABET_UPPERCASE = //
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', //
                    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', //
                    'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z'};

    private static final char[] ALPHABET_LOWERCASE = //
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', //
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', //
                    'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z'};

    private static final long[] ALPHABET_VALUES = new long[128];

    static {
        for (int i = 0; i < ALPHABET_VALUES.length; i++) {
            ALPHABET_VALUES[i] = -1;
        }
        // Numbers
        ALPHABET_VALUES['0'] = 0x00;
        ALPHABET_VALUES['1'] = 0x01;
        ALPHABET_VALUES['2'] = 0x02;
        ALPHABET_VALUES['3'] = 0x03;
        ALPHABET_VALUES['4'] = 0x04;
        ALPHABET_VALUES['5'] = 0x05;
        ALPHABET_VALUES['6'] = 0x06;
        ALPHABET_VALUES['7'] = 0x07;
        ALPHABET_VALUES['8'] = 0x08;
        ALPHABET_VALUES['9'] = 0x09;
        // Lower case
        ALPHABET_VALUES['a'] = 0x0a;
        ALPHABET_VALUES['b'] = 0x0b;
        ALPHABET_VALUES['c'] = 0x0c;
        ALPHABET_VALUES['d'] = 0x0d;
        ALPHABET_VALUES['e'] = 0x0e;
        ALPHABET_VALUES['f'] = 0x0f;
        ALPHABET_VALUES['g'] = 0x10;
        ALPHABET_VALUES['h'] = 0x11;
        ALPHABET_VALUES['j'] = 0x12;
        ALPHABET_VALUES['k'] = 0x13;
        ALPHABET_VALUES['m'] = 0x14;
        ALPHABET_VALUES['n'] = 0x15;
        ALPHABET_VALUES['p'] = 0x16;
        ALPHABET_VALUES['q'] = 0x17;
        ALPHABET_VALUES['r'] = 0x18;
        ALPHABET_VALUES['s'] = 0x19;
        ALPHABET_VALUES['t'] = 0x1a;
        ALPHABET_VALUES['v'] = 0x1b;
        ALPHABET_VALUES['w'] = 0x1c;
        ALPHABET_VALUES['x'] = 0x1d;
        ALPHABET_VALUES['y'] = 0x1e;
        ALPHABET_VALUES['z'] = 0x1f;
        // Lower case OIL
        ALPHABET_VALUES['o'] = 0x00;
        ALPHABET_VALUES['i'] = 0x01;
        ALPHABET_VALUES['l'] = 0x01;
        // Upper case
        ALPHABET_VALUES['A'] = 0x0a;
        ALPHABET_VALUES['B'] = 0x0b;
        ALPHABET_VALUES['C'] = 0x0c;
        ALPHABET_VALUES['D'] = 0x0d;
        ALPHABET_VALUES['E'] = 0x0e;
        ALPHABET_VALUES['F'] = 0x0f;
        ALPHABET_VALUES['G'] = 0x10;
        ALPHABET_VALUES['H'] = 0x11;
        ALPHABET_VALUES['J'] = 0x12;
        ALPHABET_VALUES['K'] = 0x13;
        ALPHABET_VALUES['M'] = 0x14;
        ALPHABET_VALUES['N'] = 0x15;
        ALPHABET_VALUES['P'] = 0x16;
        ALPHABET_VALUES['Q'] = 0x17;
        ALPHABET_VALUES['R'] = 0x18;
        ALPHABET_VALUES['S'] = 0x19;
        ALPHABET_VALUES['T'] = 0x1a;
        ALPHABET_VALUES['V'] = 0x1b;
        ALPHABET_VALUES['W'] = 0x1c;
        ALPHABET_VALUES['X'] = 0x1d;
        ALPHABET_VALUES['Y'] = 0x1e;
        ALPHABET_VALUES['Z'] = 0x1f;
        // Upper case OIL
        ALPHABET_VALUES['O'] = 0x00;
        ALPHABET_VALUES['I'] = 0x01;
        ALPHABET_VALUES['L'] = 0x01;
    }

    public TSID(final long number) {
        this.number = number;
    }

    public static TSID from(final long number) {
        return new TSID(number);
    }

    public static TSID from(final byte[] bytes) {

        if (bytes == null || bytes.length != TSID_BYTES) {
            throw new IllegalArgumentException("Invalid TSID bytes"); // null or wrong length!
        }

        long number = 0;

        number |= (bytes[0x0] & 0xffL) << 56;
        number |= (bytes[0x1] & 0xffL) << 48;
        number |= (bytes[0x2] & 0xffL) << 40;
        number |= (bytes[0x3] & 0xffL) << 32;
        number |= (bytes[0x4] & 0xffL) << 24;
        number |= (bytes[0x5] & 0xffL) << 16;
        number |= (bytes[0x6] & 0xffL) << 8;
        number |= (bytes[0x7] & 0xffL);

        return new TSID(number);
    }

    public static TSID from(final String string) {

        final char[] chars = toCharArray(string);

        long number = 0;

        number |= ALPHABET_VALUES[chars[0x00]] << 60;
        number |= ALPHABET_VALUES[chars[0x01]] << 55;
        number |= ALPHABET_VALUES[chars[0x02]] << 50;
        number |= ALPHABET_VALUES[chars[0x03]] << 45;
        number |= ALPHABET_VALUES[chars[0x04]] << 40;
        number |= ALPHABET_VALUES[chars[0x05]] << 35;
        number |= ALPHABET_VALUES[chars[0x06]] << 30;
        number |= ALPHABET_VALUES[chars[0x07]] << 25;
        number |= ALPHABET_VALUES[chars[0x08]] << 20;
        number |= ALPHABET_VALUES[chars[0x09]] << 15;
        number |= ALPHABET_VALUES[chars[0x0a]] << 10;
        number |= ALPHABET_VALUES[chars[0x0b]] << 5;
        number |= ALPHABET_VALUES[chars[0x0c]];

        return new TSID(number);
    }

    public long toLong() {
        return this.number;
    }

    public byte[] toBytes() {

        final byte[] bytes = new byte[TSID_BYTES];

        bytes[0x0] = (byte) (number >>> 56);
        bytes[0x1] = (byte) (number >>> 48);
        bytes[0x2] = (byte) (number >>> 40);
        bytes[0x3] = (byte) (number >>> 32);
        bytes[0x4] = (byte) (number >>> 24);
        bytes[0x5] = (byte) (number >>> 16);
        bytes[0x6] = (byte) (number >>> 8);
        bytes[0x7] = (byte) (number);

        return bytes;
    }

    public static TSID fast() {
        final long time = (System.currentTimeMillis() - TSID_EPOCH) << RANDOM_BITS;
        final long tail = LazyHolder.counter.incrementAndGet() & RANDOM_MASK;
        return new TSID(time | tail);
    }

    @Override
    public String toString() {
        return toString(ALPHABET_UPPERCASE);
    }

    public String toLowerCase() {
        return toString(ALPHABET_LOWERCASE);
    }

    public Instant getInstant() {
        return Instant.ofEpochMilli(getUnixMilliseconds());
    }

    public Instant getInstant(final Instant customEpoch) {
        return Instant.ofEpochMilli(getUnixMilliseconds(customEpoch.toEpochMilli()));
    }

    public long getUnixMilliseconds() {
        return this.getTime() + TSID_EPOCH;
    }

    public long getUnixMilliseconds(final long customEpoch) {
        return this.getTime() + customEpoch;
    }

    long getTime() {
        return this.number >>> RANDOM_BITS;
    }

    long getRandom() {
        return this.number & RANDOM_MASK;
    }

    public static boolean isValid(final String string) {
        return string != null && isValidCharArray(string.toCharArray());
    }

    @Override
    public int hashCode() {
        return (int) (number ^ (number >>> 32));
    }

    @Override
    public boolean equals(Object other) {
        if (other == null)
            return false;
        if (other.getClass() != TSID.class)
            return false;
        TSID that = (TSID) other;
        return (this.number == that.number);
    }

    @Override
    public int compareTo(TSID that) {

        // used to compare as UNSIGNED longs
        final long min = 0x8000000000000000L;

        final long a = this.number + min;
        final long b = that.number + min;

        if (a > b)
            return 1;
        else if (a < b)
            return -1;

        return 0;
    }

    public String encode(final int base) {
        if (base < 2 || base > 62) {
            throw new IllegalArgumentException(String.format("Invalid base: %s", base));
        }
        return BaseN.encode(this, base);
    }

    public static TSID decode(final String string, final int base) {
        if (base < 2 || base > 62) {
            throw new IllegalArgumentException(String.format("Invalid base: %s", base));
        }
        return BaseN.decode(string, base);
    }

    public String format(final String format) {
        if (format != null) {
            final int i = format.indexOf("%");
            if (i < 0 || i == format.length() - 1) {
                throw new IllegalArgumentException(String.format("Invalid format string: \"%s\"", format));
            }
            final String head = format.substring(0, i);
            final String tail = format.substring(i + 2);
            final char placeholder = format.charAt(i + 1);
            switch (placeholder) {
                case 'S': // canonical string in upper case
                    return head + toString() + tail;
                case 's': // canonical string in lower case
                    return head + toLowerCase() + tail;
                case 'X': // hexadecimal in upper case
                    return head + BaseN.encode(this, 16) + tail;
                case 'x': // hexadecimal in lower case
                    return head + BaseN.encode(this, 16).toLowerCase() + tail;
                case 'd': // base-10
                    return head + BaseN.encode(this, 10) + tail;
                case 'z': // base-62
                    return head + BaseN.encode(this, 62) + tail;
                default:
                    throw new IllegalArgumentException(String.format("Invalid placeholder: \"%%%s\"", placeholder));
            }
        }
        throw new IllegalArgumentException(String.format("Invalid format string: \"%s\"", format));
    }

    public static TSID unformat(final String formatted, final String format) {
        if (formatted != null && format != null) {
            final int i = format.indexOf("%");
            if (i < 0 || i == format.length() - 1) {
                throw new IllegalArgumentException(String.format("Invalid format string: \"%s\"", format));
            }
            final String head = format.substring(0, i);
            final String tail = format.substring(i + 2);
            final char placeholder = format.charAt(i + 1);
            final int length = formatted.length() - head.length() - tail.length();
            if (formatted.startsWith(head) && formatted.endsWith(tail)) {
                switch (placeholder) {
                    case 'S': // canonical string (case insensitive)
                        return TSID.from(formatted.substring(i, i + length));
                    case 's': // canonical string (case insensitive)
                        return TSID.from(formatted.substring(i, i + length));
                    case 'X': // hexadecimal (case insensitive)
                        return BaseN.decode(formatted.substring(i, i + length).toUpperCase(), 16);
                    case 'x': // hexadecimal (case insensitive)
                        return BaseN.decode(formatted.substring(i, i + length).toUpperCase(), 16);
                    case 'd': // base-10
                        return BaseN.decode(formatted.substring(i, i + length), 10);
                    case 'z': // base-62
                        return BaseN.decode(formatted.substring(i, i + length), 62);
                    default:
                        throw new IllegalArgumentException(String.format("Invalid placeholder: \"%%%s\"", placeholder));
                }
            }
        }
        throw new IllegalArgumentException(String.format("Invalid formatted string: \"%s\"", formatted));
    }

    String toString(final char[] alphabet) {

        final char[] chars = new char[TSID_CHARS];

        chars[0x00] = alphabet[(int) ((number >>> 60) & 0b11111)];
        chars[0x01] = alphabet[(int) ((number >>> 55) & 0b11111)];
        chars[0x02] = alphabet[(int) ((number >>> 50) & 0b11111)];
        chars[0x03] = alphabet[(int) ((number >>> 45) & 0b11111)];
        chars[0x04] = alphabet[(int) ((number >>> 40) & 0b11111)];
        chars[0x05] = alphabet[(int) ((number >>> 35) & 0b11111)];
        chars[0x06] = alphabet[(int) ((number >>> 30) & 0b11111)];
        chars[0x07] = alphabet[(int) ((number >>> 25) & 0b11111)];
        chars[0x08] = alphabet[(int) ((number >>> 20) & 0b11111)];
        chars[0x09] = alphabet[(int) ((number >>> 15) & 0b11111)];
        chars[0x0a] = alphabet[(int) ((number >>> 10) & 0b11111)];
        chars[0x0b] = alphabet[(int) ((number >>> 5) & 0b11111)];
        chars[0x0c] = alphabet[(int) (number & 0b11111)];

        return new String(chars);
    }

    static char[] toCharArray(final String string) {
        char[] chars = string == null ? null : string.toCharArray();
        if (!isValidCharArray(chars)) {
            throw new IllegalArgumentException(String.format("Invalid TSID string: \"%s\"", string));
        }
        return chars;
    }

    static boolean isValidCharArray(final char[] chars) {

        if (chars == null || chars.length != TSID_CHARS) {
            return false; // null or wrong size!
        }

        // The extra bit added by base-32 encoding must be zero
        // As a consequence, the 1st char of the input string must be between 0 and F.
        if ((ALPHABET_VALUES[chars[0]] & 0b10000) != 0) {
            return false; // overflow!
        }

        for (int i = 0; i < chars.length; i++) {
            if (ALPHABET_VALUES[chars[i]] == -1) {
                return false; // invalid character!
            }
        }
        return true; // It seems to be OK.
    }

    static class BaseN {

        private static final BigInteger MAX = BigInteger.valueOf(2).pow(64);
        private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"; // base-62

        static String encode(final TSID tsid, final int base) {
            BigInteger x = new BigInteger(1, tsid.toBytes());
            final BigInteger radix = BigInteger.valueOf(base);
            final int length = (int) Math.ceil(Long.SIZE / (Math.log(base) / Math.log(2)));
            int b = length; // buffer index
            char[] buffer = new char[length];
            while (x.compareTo(BigInteger.ZERO) > 0) {
                BigInteger[] result = x.divideAndRemainder(radix);
                buffer[--b] = ALPHABET.charAt(result[1].intValue());
                x = result[0];
            }
            while (b > 0) {
                buffer[--b] = '0';
            }
            return new String(buffer);
        }

        static TSID decode(final String string, final int base) {
            BigInteger x = BigInteger.ZERO;
            final BigInteger radix = BigInteger.valueOf(base);
            final int length = (int) Math.ceil(Long.SIZE / (Math.log(base) / Math.log(2)));
            if (string == null) {
                throw new IllegalArgumentException(String.format("Invalid base-%d string: null", base));
            }
            if (string.length() != length) {
                throw new IllegalArgumentException(String.format("Invalid base-%d length: %s", base, string.length()));
            }
            for (int i = 0; i < string.length(); i++) {
                final long plus = (int) ALPHABET.indexOf(string.charAt(i));
                if (plus < 0 || plus >= base) {
                    throw new IllegalArgumentException(
                            String.format("Invalid base-%d character: %s", base, string.charAt(i)));
                }
                x = x.multiply(radix).add(BigInteger.valueOf(plus));
            }
            if (x.compareTo(MAX) > 0) {
                throw new IllegalArgumentException(String.format("Invalid base-%d value (overflow): %s", base, x));
            }
            return new TSID(x.longValue());
        }
    }

    private static class LazyHolder {
        private static final AtomicInteger counter = new AtomicInteger((new SplittableRandom()).nextInt());
    }

    public static final class Factory {

        public static final Factory INSTANCE = new Factory();

        public static final Factory INSTANCE_256 = newInstance256();

        public static final Factory INSTANCE_1024 = newInstance1024();

        public static final Factory INSTANCE_4096 = newInstance4096();

        public static final IntSupplier THREAD_LOCAL_RANDOM_FUNCTION = () -> ThreadLocalRandom.current().nextInt();

        private int counter;
        private long lastTime;

        private final int node;

        private final int nodeBits;
        private final int counterBits;

        private final int nodeMask;
        private final int counterMask;

        private final Supplier<Instant> clock;
        private final long customEpoch;

        private final IRandom random;
        private final int randomBytes;

        static final int NODE_BITS_256 = 8;
        static final int NODE_BITS_1024 = 10;
        static final int NODE_BITS_4096 = 12;

        // ******************************
        // Constructors
        // ******************************

        public Factory() {
            this(builder());
        }

        public Factory(int node) {
            this(builder().withNode(node));
        }

        private Factory(Builder builder) {

            // setup node bits, custom epoch and random function
            this.customEpoch = builder.getCustomEpoch();
            this.nodeBits = builder.getNodeBits();
            this.random = builder.getRandom();
            this.clock = builder.getClock();

            // setup constants that depend on node bits
            this.counterBits = RANDOM_BITS - nodeBits;
            this.counterMask = RANDOM_MASK >>> nodeBits;
            this.nodeMask = RANDOM_MASK >>> counterBits;

            // setup how many bytes to get from the random function
            this.randomBytes = ((this.counterBits - 1) / 8) + 1;

            // setup the node identifier
            this.node = builder.getNode() & nodeMask;

            // finally, initialize internal state
            this.lastTime = clock.get().toEpochMilli();
            this.counter = getRandomValue();
        }

        public static Factory newInstance256() {
            return Factory.builder().withNodeBits(NODE_BITS_256).build();
        }

        public static Factory newInstance256(int node) {
            return Factory.builder().withNodeBits(NODE_BITS_256).withNode(node).build();
        }

        public static Factory newInstance1024() {
            return Factory.builder().withNodeBits(NODE_BITS_1024).build();
        }

        public static Factory newInstance1024(int node) {
            return Factory.builder().withNodeBits(NODE_BITS_1024).withNode(node).build();
        }

        public static Factory newInstance4096() {
            return Factory.builder().withNodeBits(NODE_BITS_4096).build();
        }

        public static Factory newInstance4096(int node) {
            return Factory.builder().withNodeBits(NODE_BITS_4096).withNode(node).build();
        }

        // ******************************
        // Public methods
        // ******************************
        public TSID generate() {

            final long _time = getTime() << RANDOM_BITS;
            final long _node = (long) this.node << this.counterBits;
            final long _counter = (long) this.counter & this.counterMask;

            return new TSID(_time | _node | _counter);
        }

        public TSID generate(long timeMillis) {

            final long _time = adjustTime(timeMillis) << RANDOM_BITS;
            final long _node = (long) this.node << this.counterBits;
            final long _counter = (long) this.counter & this.counterMask;

            return new TSID(_time | _node | _counter);
        }

        private synchronized long getTime() {

            long time = clock.get().toEpochMilli();
            return adjustTime(time);
        }

        private synchronized long adjustTime(long timeMillis) {

            if (timeMillis <= this.lastTime) {
                this.counter++;
                // Carry is 1 if an overflow occurs after ++.
                int carry = this.counter >>> this.counterBits;
                this.counter = this.counter & this.counterMask;
                timeMillis = this.lastTime + carry; // increment time
            } else {
                // If the system clock has advanced as expected,
                // simply reset the counter to a new random value.
                this.counter = this.getRandomValue();
            }

            // save current time
            this.lastTime = timeMillis;

            // adjust to the custom epoch
            return timeMillis - this.customEpoch;
        }

        private synchronized int getRandomCounter() {

            if (random instanceof ByteRandom) {

                final byte[] bytes = random.nextBytes(this.randomBytes);

                switch (bytes.length) {
                    case 1:
                        return (bytes[0] & 0xff) & this.counterMask;
                    case 2:
                        return (((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff)) & this.counterMask;
                    default:
                        return (((bytes[0] & 0xff) << 16) | ((bytes[1] & 0xff) << 8) | (bytes[2] & 0xff)) & this.counterMask;
                }

            } else {
                return random.nextInt() & this.counterMask;
            }
        }

        private int getRandomValue() {

            int randomCounter = getRandomCounter();
            int threadId = (((int) (Thread.currentThread().getId()) % 256) << (counterBits - 8));

            return (threadId | (randomCounter >> (counterBits - 8)));
        }

        public static Builder builder() {
            return new Builder();
        }

        // ******************************
        // Package-private inner classes
        // ******************************
        public static class Builder {

            private Integer node;
            private Integer nodeBits;
            private Long customEpoch;
            private IRandom random;
            private Supplier<Instant> clock;

            public Builder withNode(Integer node) {
                this.node = node;
                return this;
            }

            public Builder withNodeBits(Integer nodeBits) {
                this.nodeBits = nodeBits;
                return this;
            }

            public Builder withCustomEpoch(Instant customEpoch) {
                this.customEpoch = customEpoch.toEpochMilli();
                return this;
            }

            public Builder withRandom(Random random) {
                if (random != null) {
                    if (random instanceof SecureRandom) {
                        this.random = new ByteRandom(random);
                    } else {
                        this.random = new IntRandom(random);
                    }
                }
                return this;
            }

            public Builder withRandomFunction(IntSupplier randomFunction) {
                this.random = new IntRandom(randomFunction);
                return this;
            }

            public Builder withRandomFunction(IntFunction<byte[]> randomFunction) {
                this.random = new ByteRandom(randomFunction);
                return this;
            }

            public Builder withClock(Clock clock) {
                return withClock(clock != null ? clock::instant : null);
            }

            public Builder withClock(Supplier<Instant> clock) {
                this.clock = clock;
                return this;
            }

            protected Integer getNode() {

                final int max = (1 << nodeBits) - 1;

                if (this.node == null) {
                    if (Settings.getNode() != null) {
                        // use property or variable
                        this.node = Settings.getNode();
                    } else {
                        // use random node identifier
                        this.node = this.random.nextInt() & max;
                    }
                }

                if (node < 0 || node > max) {
                    throw new IllegalArgumentException(String.format("Node ID out of range [0, %s]: %s", max, node));
                }

                return this.node;
            }

            protected Integer getNodeBits() {

                if (this.nodeBits == null) {
                    if (Settings.getNodeCount() != null) {
                        // use property or variable
                        this.nodeBits = (int) Math.ceil(Math.log(Settings.getNodeCount()) / Math.log(2));
                    } else {
                        // use default bit length: 10 bits
                        this.nodeBits = Factory.NODE_BITS_1024;
                    }
                }

                if (nodeBits < 0 || nodeBits > 20) {
                    throw new IllegalArgumentException(String.format("Node bits out of range [0, 20]: %s", nodeBits));
                }

                return this.nodeBits;
            }

            protected Long getCustomEpoch() {
                if (this.customEpoch == null) {
                    this.customEpoch = TSID_EPOCH; // 2020-01-01
                }
                return this.customEpoch;
            }

            protected IRandom getRandom() {
                if (this.random == null) {
                    this.withRandom(new SecureRandom());
                }
                return this.random;
            }

            protected Supplier<Instant> getClock() {
                if (this.clock == null) {
                    this.withClock(Clock.systemUTC());
                }
                return this.clock;
            }

            public Factory build() {
                return new Factory(this);
            }
        }

        interface IRandom {

            int nextInt();

            byte[] nextBytes(int length);
        }

        static class IntRandom implements IRandom {

            private final IntSupplier randomFunction;

            public IntRandom() {
                this(newRandomFunction(null));
            }

            public IntRandom(Random random) {
                this(newRandomFunction(random));
            }

            public IntRandom(IntSupplier randomFunction) {
                this.randomFunction = randomFunction != null ? randomFunction : newRandomFunction(null);
            }

            @Override
            public int nextInt() {
                return randomFunction.getAsInt();
            }

            @Override
            public byte[] nextBytes(int length) {

                int shift = 0;
                long random = 0;
                final byte[] bytes = new byte[length];

                for (int i = 0; i < length; i++) {
                    if (shift < Byte.SIZE) {
                        shift = Integer.SIZE;
                        random = randomFunction.getAsInt();
                    }
                    shift -= Byte.SIZE; // 56, 48, 40...
                    bytes[i] = (byte) (random >>> shift);
                }

                return bytes;
            }

            protected static IntSupplier newRandomFunction(Random random) {
                final Random entropy = random != null ? random : new SecureRandom();
                return entropy::nextInt;
            }
        }

        static class ByteRandom implements IRandom {

            private final IntFunction<byte[]> randomFunction;

            public ByteRandom() {
                this(newRandomFunction(null));
            }

            public ByteRandom(Random random) {
                this(newRandomFunction(random));
            }

            public ByteRandom(IntFunction<byte[]> randomFunction) {
                this.randomFunction = randomFunction != null ? randomFunction : newRandomFunction(null);
            }

            @Override
            public int nextInt() {
                int number = 0;
                byte[] bytes = this.randomFunction.apply(Integer.BYTES);
                for (int i = 0; i < Integer.BYTES; i++) {
                    number = (number << 8) | (bytes[i] & 0xff);
                }
                return number;
            }

            @Override
            public byte[] nextBytes(int length) {
                return this.randomFunction.apply(length);
            }

            protected static IntFunction<byte[]> newRandomFunction(Random random) {
                final Random entropy = random != null ? random : new SecureRandom();
                return (final int length) -> {
                    final byte[] bytes = new byte[length];
                    entropy.nextBytes(bytes);
                    return bytes;
                };
            }
        }

        static class Settings {

            static final String NODE = "tsid.node";
            static final String NODE_COUNT = "tsid.node.count";

            private Settings() {
            }

            public static Integer getNode() {
                return getPropertyAsInteger(NODE);
            }

            public static Integer getNodeCount() {
                return getPropertyAsInteger(NODE_COUNT);
            }

            static Integer getPropertyAsInteger(String property) {
                try {
                    return Integer.decode(getProperty(property));
                } catch (NumberFormatException | NullPointerException e) {
                    return null;
                }
            }

            static String getProperty(String name) {

                String property = System.getProperty(name);
                if (property != null && !property.isEmpty()) {
                    return property;
                }

                String variable = System.getenv(name.toUpperCase().replace(".", "_"));
                if (variable != null && !variable.isEmpty()) {
                    return variable;
                }

                return null;
            }
        }

        public static TSID getTsid() {
            return INSTANCE.generate();
        }

        public static TSID getTsid256() {
            return INSTANCE_256.generate();
        }

        public static TSID getTsid1024() {
            return INSTANCE_1024.generate();
        }

        public static TSID getTsid4096() {
            return INSTANCE_4096.generate();
        }
    }
}
