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
import org.sollecitom.chassis.cryptography.domain.asymmetric.KEMPublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.PublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory
import org.sollecitom.chassis.cryptography.domain.factory.AsymmetricAlgorithmFamilySelector
import org.sollecitom.chassis.cryptography.domain.factory.CryptographicOperations
import org.sollecitom.chassis.cryptography.domain.factory.CrystalsAlgorithmSelector
import org.sollecitom.chassis.cryptography.domain.key.KeyMetadata
import org.sollecitom.chassis.cryptography.domain.symmetric.EncryptedData
import org.sollecitom.chassis.cryptography.domain.symmetric.EncryptionMode
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKey
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKeyWithEncapsulation
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

    override val cryptography: CryptographicOperations get() = CryptographicOperations.bouncyCastle()
}

internal const val BC_PROVIDER = BouncyCastleProvider.PROVIDER_NAME
internal val BCPQC_PROVIDER = BouncyCastlePQCProvider.PROVIDER_NAME

fun CryptographicOperations.Companion.bouncyCastle(random: SecureRandom = SecureRandom()): CryptographicOperations = BouncyCastleCryptographicOperations(random)

private class BouncyCastleCryptographicOperations(private val random: SecureRandom) : CryptographicOperations {

    override val asymmetric: AsymmetricAlgorithmFamilySelector by lazy { AsymmetricAlgorithmFamilyCustomizer(random) }

    companion object {
        init {
            if (Security.getProvider(BCPQC_PROVIDER) == null) {
                Security.addProvider(BouncyCastlePQCProvider())
            }
            if (Security.getProvider(BC_PROVIDER) == null) {
                Security.addProvider(BouncyCastleProvider())
            }
        }
    }
}

internal class AsymmetricAlgorithmFamilyCustomizer(private val random: SecureRandom) : AsymmetricAlgorithmFamilySelector {

    override val crystals: CrystalsAlgorithmSelector by lazy { CrystalsAlgorithmCustomizer(random) }
}

internal class CrystalsAlgorithmCustomizer(private val random: SecureRandom) : CrystalsAlgorithmSelector {

    override val kyber: KyberAlgorithmOperationSelector by lazy { KyberAlgorithmOperationCustomizer(random) }
}

internal class KyberAlgorithmOperationCustomizer(private val random: SecureRandom) : KyberAlgorithmOperationSelector {

    override val keyPair: KeyPairFactory<KyberKeyPairArguments, KEMPublicKey> by lazy { KyberKeyPairFactory(random) }
    override val privateKey: PrivateKeyFactory by lazy { GenericPrivateKeyFactory(Algorithms.KYBER, random) }
    override val publicKey: PublicKeyFactory<KEMPublicKey> by lazy { KEMPublicKeyFactory(Algorithms.KYBER, random) }
}

internal class GenericPrivateKeyFactory(private val algorithm: String, private val random: SecureRandom) : PrivateKeyFactory {

    override fun fromBytes(bytes: ByteArray): PrivateKey = JavaPrivateKeyAdapter.fromBytes(bytes, algorithm, random)
}

internal class KEMPublicKeyFactory(private val algorithm: String, private val random: SecureRandom) : PublicKeyFactory<KEMPublicKey> {

    override fun fromBytes(bytes: ByteArray): KEMPublicKey = JavaKEMPublicKeyAdapter.fromBytes(bytes, algorithm, random)
}

object Algorithms {

    const val KYBER = "KYBER"
}

internal class KyberKeyPairFactory(private val random: SecureRandom) : KeyPairFactory<KyberKeyPairArguments, KEMPublicKey> {

    override fun invoke(arguments: KyberKeyPairArguments): AsymmetricKeyPair<KEMPublicKey> = arguments.generateRawKeyPair().adapted(random)

    override fun fromKeys(publicKey: KEMPublicKey, privateKey: PrivateKey): AsymmetricKeyPair<KEMPublicKey> {

        require(publicKey.metadata.algorithm == Algorithms.KYBER) { "Public key algorithm must be ${Algorithms.KYBER}" }
        require(privateKey.metadata.algorithm == Algorithms.KYBER) { "Private key algorithm must be ${Algorithms.KYBER}" }
        return KeyPair(publicKey, privateKey)
    }

    private val Variant.spec: KyberParameterSpec
        get() = when (this) {
            Variant.KYBER_512 -> KyberParameterSpec.kyber512
            Variant.KYBER_768 -> KyberParameterSpec.kyber768
            Variant.KYBER_1024 -> KyberParameterSpec.kyber1024
            Variant.KYBER_512_AES -> KyberParameterSpec.kyber512_aes
            Variant.KYBER_768_AES -> KyberParameterSpec.kyber768_aes
            Variant.KYBER_1024_AES -> KyberParameterSpec.kyber512_aes
        }

    private fun Variant.generateRawKeyPair(): JavaKeyPair = generateKeyPair(Algorithms.KYBER, spec, random)

    private fun KyberKeyPairArguments.generateRawKeyPair() = variant.generateRawKeyPair()
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

private fun JavaKeyPair.adapted(random: SecureRandom) = KeyPair(public = public.asKEMPublicKey(random), private = private.adapted(random))

private fun JavaPrivateKey.adapted(random: SecureRandom) = JavaPrivateKeyAdapter(this, random)
private fun JavaPublicKey.asKEMPublicKey(random: SecureRandom): KEMPublicKey = JavaKEMPublicKeyAdapter(this, random)

private data class KeyPair<PUBLIC : PublicKey>(override val public: PUBLIC, override val private: PrivateKey) : AsymmetricKeyPair<PUBLIC> {

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaPrivateKeyAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()


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

        return encoded.contentEquals(other.encoded)
    }

    override fun hashCode() = encoded.contentHashCode()

    override fun toString() = "JavaAESKeyAdapter(encoded=${encoded.contentToString()}, keySpec=${keySpec})"

    companion object {
        private const val ALGORITHM = "AES"
    }
}

private data class JavaKeySpecMetadataAdapter(private val keySpec: SecretKeySpec) : KeyMetadata {

    override val algorithm: String get() = keySpec.algorithm
    override val format: String get() = keySpec.format

}

private data class JavaKEMPublicKeyAdapter(private val key: JavaPublicKey, private val random: SecureRandom) : KEMPublicKey {

    override val encoded: ByteArray get() = key.encoded
    override val metadata: KeyMetadata = JavaKeyMetadataAdapter(key)

    override fun generateEncapsulatedAESKey(): SymmetricKeyWithEncapsulation {

        val rawKeyAndEncapsulation = generateAESEncryptionKey(key, metadata.algorithm, random)
        return SymmetricKeyWithEncapsulation(key = JavaAESKeyAdapter(rawKeyAndEncapsulation.encoded, random), encapsulation = rawKeyAndEncapsulation.encapsulation)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as JavaKEMPublicKeyAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()


    companion object {

        fun fromBytes(bytes: ByteArray, algorithm: String, random: SecureRandom): JavaKEMPublicKeyAdapter = getPublicKeyFromEncoded(bytes, algorithm).let { JavaKEMPublicKeyAdapter(it, random) }
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CTROperationsAdapter

        return key == other.key
    }

    override fun hashCode() = key.hashCode()

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