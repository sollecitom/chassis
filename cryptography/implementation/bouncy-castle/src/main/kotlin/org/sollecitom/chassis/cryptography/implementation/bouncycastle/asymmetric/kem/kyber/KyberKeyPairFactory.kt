package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.kyber

import org.bouncycastle.pqc.jcajce.spec.KyberParameterSpec
import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.Kyber
import org.sollecitom.chassis.cryptography.domain.asymmetric.*
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.Algorithms
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.JavaKEMPrivateKeyAdapter
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.JavaKEMPublicKeyAdapter
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.PublicKey
import java.security.SecureRandom
import java.security.KeyPair as JavaKeyPair
import java.security.PrivateKey as JavaPrivateKey

internal class KyberKeyPairFactory(private val random: SecureRandom) : KeyPairFactory<Kyber.KeyPairArguments, KEMPrivateKey, KEMPublicKey> {

    override fun invoke(arguments: Kyber.KeyPairArguments): AsymmetricKeyPair<KEMPrivateKey, KEMPublicKey> = arguments.generateRawKeyPair().asKEMPrivateKey(random)

    override fun fromKeys(publicKey: KEMPublicKey, privateKey: KEMPrivateKey): AsymmetricKeyPair<KEMPrivateKey, KEMPublicKey> {

        require(privateKey.metadata.algorithm == Algorithms.Kyber.NAME) { "Private key algorithm must be ${Algorithms.Kyber.NAME}" }
        require(publicKey.metadata.algorithm == Algorithms.Kyber.NAME) { "Public key algorithm must be ${Algorithms.Kyber.NAME}" }
        return KeyPair(privateKey, publicKey)
    }

    private val Kyber.Variant.spec: KyberParameterSpec
        get() = when (this) {
            Kyber.Variant.KYBER_512 -> KyberParameterSpec.kyber512
            Kyber.Variant.KYBER_768 -> KyberParameterSpec.kyber768
            Kyber.Variant.KYBER_1024 -> KyberParameterSpec.kyber1024
            Kyber.Variant.KYBER_512_AES -> KyberParameterSpec.kyber512_aes
            Kyber.Variant.KYBER_768_AES -> KyberParameterSpec.kyber768_aes
            Kyber.Variant.KYBER_1024_AES -> KyberParameterSpec.kyber512_aes
        }

    private fun Kyber.Variant.generateRawKeyPair(): JavaKeyPair = BouncyCastleUtils.generateKeyPair(Algorithms.Kyber.NAME, spec, random)

    private fun Kyber.KeyPairArguments.generateRawKeyPair() = variant.generateRawKeyPair()

    private fun JavaKeyPair.asKEMPrivateKey(random: SecureRandom) = KeyPair(private = private.asKEMPrivateKey(random), public = public.asKEMPublicKey(random))

    private fun JavaPrivateKey.asKEMPrivateKey(random: SecureRandom): KEMPrivateKey = JavaKEMPrivateKeyAdapter(this, random)
    private fun PublicKey.asKEMPublicKey(random: SecureRandom): KEMPublicKey = JavaKEMPublicKeyAdapter(this, random)
}