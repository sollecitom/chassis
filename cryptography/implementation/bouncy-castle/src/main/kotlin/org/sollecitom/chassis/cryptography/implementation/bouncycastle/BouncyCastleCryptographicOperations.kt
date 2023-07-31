package org.sollecitom.chassis.cryptography.implementation.bouncycastle

import org.bouncycastle.jcajce.SecretKeyWithEncapsulation
import org.bouncycastle.jcajce.spec.KEMExtractSpec
import org.bouncycastle.jcajce.spec.KEMGenerateSpec
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider
import org.sollecitom.chassis.cryptography.domain.factory.AsymmetricAlgorithmFamilySelector
import org.sollecitom.chassis.cryptography.domain.factory.CryptographicOperations
import org.sollecitom.chassis.cryptography.domain.symmetric.EncryptionMode
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
import java.security.KeyPair as JavaKeyPair
import java.security.PrivateKey as JavaPrivateKey
import java.security.PublicKey as JavaPublicKey

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

fun CryptographicOperations.Companion.bouncyCastle(random: SecureRandom = SecureRandom()): CryptographicOperations = BouncyCastleCryptographicOperations(random)

// TODO move all of these
fun generateAESEncryptionKey(publicKey: JavaPublicKey, algorithm: String, random: SecureRandom): SecretKeyWithEncapsulation {

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

fun generateKeyPair(algorithm: String, spec: AlgorithmParameterSpec, random: SecureRandom): JavaKeyPair {

    val kpg = KeyPairGenerator.getInstance(algorithm, BCPQC_PROVIDER)
    kpg.initialize(spec, random)
    return kpg.generateKeyPair()
}

fun getPrivateKeyFromEncoded(encodedKey: ByteArray, algorithm: String): JavaPrivateKey {

    val pkcs8EncodedKeySpec = PKCS8EncodedKeySpec(encodedKey)
    val keyFactory = KeyFactory.getInstance(algorithm, BCPQC_PROVIDER)
    return keyFactory.generatePrivate(pkcs8EncodedKeySpec)
}

fun getPublicKeyFromEncoded(encodedKey: ByteArray, algorithm: String): JavaPublicKey {

    val x509EncodedKeySpec = X509EncodedKeySpec(encodedKey)
    val keyFactory: KeyFactory = KeyFactory.getInstance(algorithm, BCPQC_PROVIDER)
    return keyFactory.generatePublic(x509EncodedKeySpec)
}

// TODO move
fun ctrEncrypt(key: SecretKey, iv: ByteArray, data: ByteArray): Pair<ByteArray, ByteArray> {
    val cipher = Cipher.getInstance("AES/CTR/NoPadding", BC_PROVIDER)
    cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
    return cipher.iv to cipher.doFinal(data)
}

// TODO move
fun ctrDecrypt(key: SecretKey, iv: ByteArray, cipherText: ByteArray): ByteArray {
    val cipher = Cipher.getInstance("AES/CTR/NoPadding", BC_PROVIDER)
    cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
    return cipher.doFinal(cipherText)
}