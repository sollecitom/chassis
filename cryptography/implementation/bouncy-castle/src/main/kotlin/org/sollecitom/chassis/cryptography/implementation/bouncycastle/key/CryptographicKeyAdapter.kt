package org.sollecitom.chassis.cryptography.implementation.bouncycastle.key

import org.sollecitom.chassis.cryptography.domain.key.CryptographicKey
import org.sollecitom.chassis.hashing.utils.xxh.Xxh3
import java.security.Key
import java.util.*

@JvmInline
value class CryptographicKeyAdapter(val key: Key) : CryptographicKey {

    override val encoded: ByteArray get() = key.encoded
    override val encodedAsHexString: String get() = HexFormat.of().formatHex(encoded)
    override val algorithm: String get() = key.algorithm
    override val format: String get() = key.format
    override val hash: Long get() = Xxh3.hash64(encoded)
}