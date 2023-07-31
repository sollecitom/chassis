package org.sollecitom.chassis.cryptography.implementation.bouncycastle

import org.bouncycastle.jcajce.SecretKeyWithEncapsulation
import org.bouncycastle.jcajce.spec.KEMExtractSpec
import org.bouncycastle.jcajce.spec.KEMGenerateSpec
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider
import org.bouncycastle.pqc.jcajce.spec.KyberParameterSpec
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.KyberAlgorithmOperationSelector
import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.KyberKeyPairArguments
import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.KyberKeyPairArguments.Variant
import org.sollecitom.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair
import org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.PublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory
import org.sollecitom.chassis.cryptography.domain.factory.AsymmetricAlgorithmFamilySelector
import org.sollecitom.chassis.cryptography.domain.factory.CryptographyCategorySelector
import org.sollecitom.chassis.cryptography.domain.factory.CrystalsAlgorithmSelector
import org.sollecitom.chassis.cryptography.domain.key.KeyMetadata
import org.sollecitom.chassis.cryptography.domain.symmetric.*
import org.sollecitom.chassis.cryptography.test.specification.CryptographyTestSpecification
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.Security
import java.security.spec.AlgorithmParameterSpec
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import java.security.Key as JavaKey
import java.security.KeyPair as JavaKeyPair
import java.security.PrivateKey as JavaPrivateKey
import java.security.PublicKey as JavaPublicKey


@TestInstance(PER_CLASS)
private class BouncyCastleCryptographyExampleTests : CryptographyTestSpecification {

    override fun newCryptographyKeysFactory(): CryptographyCategorySelector = implementation
}

private val implementation = BouncyCastleCryptographyImplementation

private const val BC_PROVIDER = BouncyCastleProvider.PROVIDER_NAME
private val BCPQC_PROVIDER = BouncyCastlePQCProvider.PROVIDER_NAME

// TODO refactor this whole mess
// TODO turn into a class?
object BouncyCastleCryptographyImplementation : CryptographyCategorySelector {

    private val random = SecureRandom() // TODO pass a seed?

    init {
        if (Security.getProvider(BCPQC_PROVIDER) == null) {
            Security.addProvider(BouncyCastlePQCProvider())
        }
        if (Security.getProvider(BC_PROVIDER) == null) {
            Security.addProvider(BouncyCastleProvider())
        }
    }

    override val asymmetric: AsymmetricAlgorithmFamilySelector get() = Asymmetric

    private object Asymmetric : AsymmetricAlgorithmFamilySelector {

        override val crystals: CrystalsAlgorithmSelector get() = Crystals

        private object Crystals : CrystalsAlgorithmSelector {

            override val kyber: KyberAlgorithmOperationSelector get() = Kyber

            private object Kyber : KyberAlgorithmOperationSelector {

                private const val ALGORITHM = "KYBER"
                override val keyPair: KeyPairFactory<KyberKeyPairArguments> get() = KeyPairFact
                override val privateKey: PrivateKeyFactory get() = PrivateKeyFact
                override val publicKey: PublicKeyFactory get() = PublicKeyFact

                private object KeyPairFact : KeyPairFactory<KyberKeyPairArguments> {

                    override fun invoke(arguments: KyberKeyPairArguments): AsymmetricKeyPair = arguments.generateRawKeyPair().adapted(random)

                    override fun fromKeys(publicKey: PublicKey, privateKey: PrivateKey): AsymmetricKeyPair {

                        require(publicKey.metadata.algorithm == ALGORITHM) { "Public key algorithm must be $ALGORITHM" }
                        require(privateKey.metadata.algorithm == ALGORITHM) { "Private key algorithm must be $ALGORITHM" }
                        return KeyPair(publicKey, privateKey)
                    }

                    private val Variant.spec: KyberParameterSpec
                        get() = when (this) {
                            Variant.KYBER_512 -> KyberParameterSpec.kyber512
                            Variant.KYBER_768 -> KyberParameterSpec.kyber768
                            Variant.KYBER_1024 -> KyberParameterSpec.kyber1024
                        }

                    private fun Variant.generateRawKeyPair(): JavaKeyPair = generateKeyPair(ALGORITHM, spec, random)

                    private fun KyberKeyPairArguments.generateRawKeyPair() = variant.generateRawKeyPair()
                }

                private object PrivateKeyFact : PrivateKeyFactory {

                    override fun fromBytes(bytes: ByteArray): PrivateKey = JavaPrivateKeyAdapter.fromBytes(bytes, ALGORITHM, random)
                }

                private object PublicKeyFact : PublicKeyFactory {

                    override fun fromBytes(bytes: ByteArray): PublicKey = JavaPublicKeyAdapter.fromBytes(bytes, ALGORITHM, random)
                }
            }
        }
    }
}

private fun generateAESEncryptionKey(publicKey: JavaPublicKey, algorithm: String, random: SecureRandom): SecretKeyWithEncapsulation {

    val keyGen = KeyGenerator.getInstance(algorithm, BCPQC_PROVIDER)
    keyGen.init(KEMGenerateSpec(publicKey, "AES"), random)
    return keyGen.generateKey() as SecretKeyWithEncapsulation
}

fun decryptEncapsulatedAESKey(privateKey: JavaPrivateKey, encapsulatedKey: ByteArray, algorithm: String, random: SecureRandom): ByteArray {
    val keyGen = KeyGenerator.getInstance(algorithm, BCPQC_PROVIDER)
    keyGen.init(KEMExtractSpec(privateKey, encapsulatedKey, "AES"), random)
    val secEnc = keyGen.generateKey() as SecretKeyWithEncapsulation
    return secEnc.encoded
}

private fun generateKeyPair(algorithm: String, spec: AlgorithmParameterSpec, random: SecureRandom): JavaKeyPair {

    val kpg = KeyPairGenerator.getInstance(algorithm, BCPQC_PROVIDER)
    kpg.initialize(spec, random)
    return kpg.generateKeyPair()
}

private fun getPrivateKeyFromEncoded(encodedKey: ByteArray, algorithm: String): JavaPrivateKey {

    val pkcs8EncodedKeySpec = PKCS8EncodedKeySpec(encodedKey)
    val keyFactory = KeyFactory.getInstance(algorithm, BCPQC_PROVIDER)
    return keyFactory.generatePrivate(pkcs8EncodedKeySpec)
}

private fun getPublicKeyFromEncoded(encodedKey: ByteArray, algorithm: String): JavaPublicKey {

    val x509EncodedKeySpec = X509EncodedKeySpec(encodedKey)
    val keyFactory: KeyFactory = KeyFactory.getInstance(algorithm, BCPQC_PROVIDER)
    return keyFactory.generatePublic(x509EncodedKeySpec)
}

private fun JavaKeyPair.adapted(random: SecureRandom) = KeyPair(public = public.adapted(random), private = private.adapted(random))

private fun JavaPrivateKey.adapted(random: SecureRandom) = JavaPrivateKeyAdapter(this, random)
private fun JavaPublicKey.adapted(random: SecureRandom) = JavaPublicKeyAdapter(this, random)

private data class KeyPair(override val public: PublicKey, override val private: PrivateKey) : AsymmetricKeyPair {

    init {
        require(public.metadata.algorithm == private.metadata.algorithm) { "Public and private key must have the same algorithm" }
    }
}

private data class JavaPrivateKeyAdapter(private val key: JavaPrivateKey, private val random: SecureRandom) : PrivateKey {

    override val encoded: ByteArray get() = key.encoded
    override val metadata: KeyMetadata = JavaKeyMetadataAdapter(key)

    override fun decryptEncapsulatedAESKey(encapsulatedKey: ByteArray): SymmetricKey {

        val rawEncodedSymmetricKey = decryptEncapsulatedAESKey(key, encapsulatedKey, metadata.algorithm, random)
        return JavaAESKeyAdapter(rawEncodedSymmetricKey, random)
    }

    companion object {

        fun fromBytes(bytes: ByteArray, algorithm: String, random: SecureRandom): JavaPrivateKeyAdapter = getPrivateKeyFromEncoded(bytes, algorithm).let { JavaPrivateKeyAdapter(it, random) }
    }
}

private data class JavaAESKeyAdapter(override val encoded: ByteArray, private val random: SecureRandom) : SymmetricKey {

    private val keySpec = SecretKeySpec(encoded, ALGORITHM)
    override val metadata: KeyMetadata = JavaKeySpecMetadataAdapter(keySpec)
    override val ctr: EncryptionMode.CTR.Operations by lazy { EncryptionMode.CTR.Operations.create(keySpec, random) }

    init {
        require(metadata.algorithm == ALGORITHM) { "Key algorithm must be $ALGORITHM" }
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaAESKeyAdapter

        if (!encoded.contentEquals(other.encoded)) return false
        if (random != other.random) return false

        return true
    }

    override fun hashCode(): Int {
        var result = encoded.contentHashCode()
        result = 31 * result + random.hashCode()
        return result
    }

    override fun toString() = "JavaAESKeyAdapter(encoded=${encoded.contentToString()}, random=$random)"

    companion object {
        private const val ALGORITHM = "AES"
    }
}

private data class JavaKeySpecMetadataAdapter(private val keySpec: SecretKeySpec) : KeyMetadata {

    override val algorithm: String get() = keySpec.algorithm
    override val format: String get() = keySpec.format

}

private data class JavaPublicKeyAdapter(private val key: JavaPublicKey, private val random: SecureRandom) : PublicKey {

    override val encoded: ByteArray get() = key.encoded
    override val metadata: KeyMetadata = JavaKeyMetadataAdapter(key)

    override fun generateEncapsulatedAESKey(): SymmetricKeyWithEncapsulation {

        val rawKeyAndEncapsulation = generateAESEncryptionKey(key, metadata.algorithm, random)
        return SymmetricKeyWithEncapsulation(key = JavaAESKeyAdapter(rawKeyAndEncapsulation.encoded, random), encapsulation = rawKeyAndEncapsulation.encapsulation)
    }

    companion object {

        fun fromBytes(bytes: ByteArray, algorithm: String, random: SecureRandom): JavaPublicKeyAdapter = getPublicKeyFromEncoded(bytes, algorithm).let { JavaPublicKeyAdapter(it, random) }
    }
}

private data class JavaKeyMetadataAdapter(private val key: JavaKey) : KeyMetadata {

    override val algorithm: String get() = key.algorithm
    override val format: String get() = key.format
}

private class CTROperationsAdapter(private val key: SecretKey, private val random: SecureRandom) : EncryptionMode.CTR.Operations {

    override fun encryptWithRandomIV(bytes: ByteArray) = encrypt(bytes = bytes, iv = newIv())

    override fun encrypt(bytes: ByteArray, iv: ByteArray): EncryptedData<EncryptionMode.CTR.Metadata> {

        val (_, encrypted) = ctrEncrypt(key, iv, bytes)
        return EncryptedData(content = encrypted, EncryptionMode.CTR.Metadata(iv = iv))
    }

    override fun decrypt(bytes: ByteArray, iv: ByteArray): ByteArray = ctrDecrypt(key = key, iv = iv, cipherText = bytes)

    private fun newIv() = random.generateSeed(RANDOM_IV_LENGTH)

    companion object {
        private const val RANDOM_IV_LENGTH = 16
    }
}

fun EncryptionMode.CTR.Operations.Companion.create(key: SecretKey, random: SecureRandom): EncryptionMode.CTR.Operations = CTROperationsAdapter(key, random)

// TODO move
private fun ctrEncrypt(key: SecretKey, iv: ByteArray, data: ByteArray): Pair<ByteArray, ByteArray> {
    val cipher = Cipher.getInstance("AES/CTR/NoPadding", BC_PROVIDER)
    cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
    return cipher.iv to cipher.doFinal(data)
}

// TODO move
private fun ctrDecrypt(key: SecretKey, iv: ByteArray, cipherText: ByteArray): ByteArray {
    val cipher = Cipher.getInstance("AES/CTR/NoPadding", BC_PROVIDER)
    cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
    return cipher.doFinal(cipherText)
}