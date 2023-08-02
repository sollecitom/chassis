package org.sollecitom.chassis.cryptography.implementation.bouncycastle.symmetric.encryption.aes

import org.sollecitom.chassis.cryptography.domain.key.CryptographicKey
import org.sollecitom.chassis.cryptography.domain.symmetric.EncryptionMode
import org.sollecitom.chassis.cryptography.domain.symmetric.SecretKeyFactory
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKey
import org.sollecitom.chassis.cryptography.domain.symmetric.encryption.aes.AES
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.BC_PROVIDER
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.create
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.key.CryptographicKeyAdapter
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.SecureRandom
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

// TODO replace invocations with factory
internal data class AESKeyAdapter(private val keySpec: SecretKey, private val random: SecureRandom) : SymmetricKey, CryptographicKey by CryptographicKeyAdapter(keySpec) {

    constructor(encoded: ByteArray, random: SecureRandom) : this(SecretKeySpec(encoded, AES.name), random)

    override val ctr: EncryptionMode.CTR.Operations by lazy { EncryptionMode.CTR.Operations.create(keySpec, random) }

    init {
        require(algorithm == AES.name) { "Key algorithm must be ${AES.name}" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AESKeyAdapter

        return encoded.contentEquals(other.encoded)
    }

    override fun hashCode() = encoded.contentHashCode()

    override fun toString() = "JavaAESKeyAdapter(encoded=${encoded.contentToString()}, keySpec=${keySpec})"

    data class Factory(val random: SecureRandom) : SecretKeyFactory<AES.KeyArguments, SymmetricKey> {

        override fun invoke(arguments: AES.KeyArguments): SymmetricKey {

            val rawKey = BouncyCastleUtils.generateSecretKey(algorithm = AES.name, length = arguments.variant.keyLength, provider = BC_PROVIDER)
            return AESKeyAdapter(keySpec = rawKey, random = random)
        }

        override fun from(bytes: ByteArray): SymmetricKey = AESKeyAdapter(encoded = bytes, random = random)
    }
}