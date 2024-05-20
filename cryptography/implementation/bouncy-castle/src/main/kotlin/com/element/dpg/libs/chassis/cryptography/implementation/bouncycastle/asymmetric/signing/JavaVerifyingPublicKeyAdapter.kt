package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.signing

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import com.element.dpg.libs.chassis.cryptography.domain.key.CryptographicKey
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.BC_PROVIDER
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.key.CryptographicKeyAdapter
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.PublicKey
import java.security.SecureRandom

internal data class JavaVerifyingPublicKeyAdapter(private val key: PublicKey, private val random: SecureRandom) : VerifyingPublicKey, CryptographicKey by CryptographicKeyAdapter(key) {

    override fun verify(input: ByteArray, signatureBytes: ByteArray, signatureAlgorithm: String): Boolean {

        return BouncyCastleUtils.verifySignature(publicKey = key, message = input, signature = signatureBytes, signatureAlgorithm = signatureAlgorithm, provider = BC_PROVIDER)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaVerifyingPublicKeyAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()


    companion object {

        fun from(bytes: ByteArray, algorithm: String, random: SecureRandom): JavaVerifyingPublicKeyAdapter = BouncyCastleUtils.getPublicKeyFromEncoded(bytes, algorithm).let { JavaVerifyingPublicKeyAdapter(it, random) }
    }
}