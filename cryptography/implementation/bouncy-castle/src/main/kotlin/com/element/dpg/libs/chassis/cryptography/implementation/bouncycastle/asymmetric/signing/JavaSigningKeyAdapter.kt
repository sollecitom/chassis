package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.signing

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.Signature
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.key.CryptographicKey
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.BC_PROVIDER
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.key.CryptographicKeyAdapter
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.SecureRandom
import java.security.PrivateKey as JavaPrivateKey

internal data class JavaSigningKeyAdapter(private val key: JavaPrivateKey, private val random: SecureRandom) : SigningPrivateKey, CryptographicKey by CryptographicKeyAdapter(key) {

    override fun sign(input: ByteArray): Signature {

        val bytes = BouncyCastleUtils.sign(privateKey = key, message = input, signatureAlgorithm = key.algorithm, provider = BC_PROVIDER)
        return Signature(bytes = bytes, metadata = Signature.Metadata(hash, key.algorithm))
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaSigningKeyAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()

    companion object {

        fun from(bytes: ByteArray, random: SecureRandom, algorithm: String): JavaSigningKeyAdapter = BouncyCastleUtils.getPrivateKeyFromEncoded(bytes, algorithm).let { JavaSigningKeyAdapter(it, random) }
    }
}