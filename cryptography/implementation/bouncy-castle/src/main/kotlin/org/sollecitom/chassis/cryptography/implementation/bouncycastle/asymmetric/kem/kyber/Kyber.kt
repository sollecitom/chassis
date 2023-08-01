package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.kyber

import org.bouncycastle.pqc.jcajce.spec.KyberParameterSpec
import org.sollecitom.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair
import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.KeyPairGenerationOperations
import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.kyber.Kyber
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.*
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom

object Kyber : KEMAlgorithm<Kyber.KeyPairArguments, KEMPrivateKey, KEMPublicKey> {

    override fun keyPairGenerationOperations(random: SecureRandom): KeyPairGenerationOperations<Kyber.KeyPairArguments, KEMPrivateKey, KEMPublicKey> = KyberAlgorithmOperationCustomizer(random)
}

private class KyberAlgorithmOperationCustomizer(private val random: SecureRandom) : KeyPairGenerationOperations<Kyber.KeyPairArguments, KEMPrivateKey, KEMPublicKey> {

    override val keyPair by lazy { KyberKeyPairFactory(random) }
    override val privateKey by lazy { KEMPrivateKeyFactory(Kyber.NAME, random) }
    override val publicKey by lazy { KEMPublicKeyFactory(Kyber.NAME, random) }
}

private class KyberKeyPairFactory(private val random: SecureRandom) : KeyPairFactory<Kyber.KeyPairArguments, KEMPrivateKey, KEMPublicKey> {

    override fun invoke(arguments: Kyber.KeyPairArguments): AsymmetricKeyPair<KEMPrivateKey, KEMPublicKey> = arguments.generateRawKeyPair().asKEMKeyPair(random)

    override fun fromKeys(privateKey: KEMPrivateKey, publicKey: KEMPublicKey): AsymmetricKeyPair<KEMPrivateKey, KEMPublicKey> {

        require(privateKey.algorithm == Kyber.NAME) { "Private key algorithm must be ${Kyber.NAME}" }
        require(publicKey.algorithm == Kyber.NAME) { "Public key algorithm must be ${Kyber.NAME}" }
        return org.sollecitom.chassis.cryptography.domain.asymmetric.KeyPair(privateKey, publicKey)
    }

    private val Kyber.Variant.spec: KyberParameterSpec
        get() = when (this) {
            Kyber.Variant.KYBER_512 -> KyberParameterSpec.kyber512
            Kyber.Variant.KYBER_768 -> KyberParameterSpec.kyber768
            Kyber.Variant.KYBER_1024 -> KyberParameterSpec.kyber1024
            Kyber.Variant.KYBER_512_AES -> KyberParameterSpec.kyber512_aes
            Kyber.Variant.KYBER_768_AES -> KyberParameterSpec.kyber768_aes
            Kyber.Variant.KYBER_1024_AES -> KyberParameterSpec.kyber1024_aes
        }

    private fun Kyber.Variant.generateRawKeyPair(): KeyPair = BouncyCastleUtils.generateKeyPair(Kyber.NAME, spec, random)

    private fun Kyber.KeyPairArguments.generateRawKeyPair() = variant.generateRawKeyPair()

    private fun KeyPair.asKEMKeyPair(random: SecureRandom) = org.sollecitom.chassis.cryptography.domain.asymmetric.KeyPair(private = private.asKEMPrivateKey(random), public = public.asKEMPublicKey(random))

    private fun PrivateKey.asKEMPrivateKey(random: SecureRandom): KEMPrivateKey = JavaKEMPrivateKeyAdapter(this, random)

    private fun PublicKey.asKEMPublicKey(random: SecureRandom): KEMPublicKey = JavaKEMPublicKeyAdapter(this, random)
}