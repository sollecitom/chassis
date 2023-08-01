package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.kyber

import org.bouncycastle.pqc.jcajce.spec.KyberParameterSpec
import org.sollecitom.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair
import org.sollecitom.chassis.cryptography.domain.asymmetric.KeyPair
import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.KeyPairGenerationOperations
import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.kyber.Kyber
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.*
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec
import java.security.KeyPair as JavaKeyPair

object Kyber : KEMAlgorithm<Kyber.KeyPairArguments, KEMPrivateKey, KEMPublicKey> {

    override val name: String get() = Kyber.name

    override fun keyPairGenerationOperations(random: SecureRandom): KeyPairGenerationOperations<Kyber.KeyPairArguments, KEMPrivateKey, KEMPublicKey> = KyberAlgorithmOperationCustomizer(random)
}

private class KyberAlgorithmOperationCustomizer(private val random: SecureRandom) : KeyPairGenerationOperations<Kyber.KeyPairArguments, KEMPrivateKey, KEMPublicKey> {

    override val keyPair by lazy { KEMKeyPairFactory<Kyber.KeyPairArguments>(Kyber.name, random) { variant.spec } }
    override val privateKey by lazy { KEMPrivateKeyFactory(Kyber.name, random) }
    override val publicKey by lazy { KEMPublicKeyFactory(Kyber.name, random) }

    private val Kyber.Variant.spec: KyberParameterSpec
        get() = when (this) {
            Kyber.Variant.KYBER_512 -> KyberParameterSpec.kyber512
            Kyber.Variant.KYBER_768 -> KyberParameterSpec.kyber768
            Kyber.Variant.KYBER_1024 -> KyberParameterSpec.kyber1024
            Kyber.Variant.KYBER_512_AES -> KyberParameterSpec.kyber512_aes
            Kyber.Variant.KYBER_768_AES -> KyberParameterSpec.kyber768_aes
            Kyber.Variant.KYBER_1024_AES -> KyberParameterSpec.kyber1024_aes
        }
}

private class KEMKeyPairFactory<ARGUMENTS>(private val algorithm: String, private val random: SecureRandom, private val spec: ARGUMENTS.() -> AlgorithmParameterSpec) : KeyPairFactory<ARGUMENTS, KEMPrivateKey, KEMPublicKey> {

    override fun invoke(arguments: ARGUMENTS): AsymmetricKeyPair<KEMPrivateKey, KEMPublicKey> = arguments.generateRawKeyPair().asKEMKeyPair(random)

    override fun fromKeys(privateKey: KEMPrivateKey, publicKey: KEMPublicKey): AsymmetricKeyPair<KEMPrivateKey, KEMPublicKey> {

        require(privateKey.algorithm == algorithm) { "Private key algorithm must be $algorithm" }
        require(publicKey.algorithm == algorithm) { "Public key algorithm must be $algorithm" }
        return KeyPair(privateKey, publicKey)
    }

    private fun ARGUMENTS.generateRawKeyPair(): JavaKeyPair = BouncyCastleUtils.generateKeyPair(Kyber.name, spec(), random)

    private fun JavaKeyPair.asKEMKeyPair(random: SecureRandom) = KeyPair(private = private.asKEMPrivateKey(random), public = public.asKEMPublicKey(random))

    private fun PrivateKey.asKEMPrivateKey(random: SecureRandom): KEMPrivateKey = JavaKEMPrivateKeyAdapter(this, random)

    private fun PublicKey.asKEMPublicKey(random: SecureRandom): KEMPublicKey = JavaKEMPublicKeyAdapter(this, random)
}