package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.kyber

import org.bouncycastle.pqc.jcajce.spec.KyberParameterSpec
import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.KyberKeyPairArguments
import org.sollecitom.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair
import org.sollecitom.chassis.cryptography.domain.asymmetric.KEMPublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.KeyPair
import org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.Algorithms
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.JavaPrivateKeyAdapter
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.JavaKEMPublicKeyAdapter
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.PublicKey
import java.security.SecureRandom
import java.security.KeyPair as JavaKeyPair
import java.security.PrivateKey as JavaPrivateKey

internal class KyberKeyPairFactory(private val random: SecureRandom) : KeyPairFactory<KyberKeyPairArguments, KEMPublicKey> {

    override fun invoke(arguments: KyberKeyPairArguments): AsymmetricKeyPair<KEMPublicKey> = arguments.generateRawKeyPair().adapted(random)

    override fun fromKeys(publicKey: KEMPublicKey, privateKey: PrivateKey): AsymmetricKeyPair<KEMPublicKey> {

        require(publicKey.metadata.algorithm == Algorithms.KYBER) { "Public key algorithm must be ${Algorithms.KYBER}" }
        require(privateKey.metadata.algorithm == Algorithms.KYBER) { "Private key algorithm must be ${Algorithms.KYBER}" }
        return KeyPair(publicKey, privateKey)
    }

    private val KyberKeyPairArguments.Variant.spec: KyberParameterSpec
        get() = when (this) {
            KyberKeyPairArguments.Variant.KYBER_512 -> KyberParameterSpec.kyber512
            KyberKeyPairArguments.Variant.KYBER_768 -> KyberParameterSpec.kyber768
            KyberKeyPairArguments.Variant.KYBER_1024 -> KyberParameterSpec.kyber1024
            KyberKeyPairArguments.Variant.KYBER_512_AES -> KyberParameterSpec.kyber512_aes
            KyberKeyPairArguments.Variant.KYBER_768_AES -> KyberParameterSpec.kyber768_aes
            KyberKeyPairArguments.Variant.KYBER_1024_AES -> KyberParameterSpec.kyber512_aes
        }

    private fun KyberKeyPairArguments.Variant.generateRawKeyPair(): JavaKeyPair = BouncyCastleUtils.generateKeyPair(Algorithms.KYBER, spec, random)

    private fun KyberKeyPairArguments.generateRawKeyPair() = variant.generateRawKeyPair()

    private fun JavaKeyPair.adapted(random: SecureRandom) = KeyPair(public = public.asKEMPublicKey(random), private = private.adapted(random))

    private fun JavaPrivateKey.adapted(random: SecureRandom) = JavaPrivateKeyAdapter(this, random)
    private fun PublicKey.asKEMPublicKey(random: SecureRandom): KEMPublicKey = JavaKEMPublicKeyAdapter(this, random)
}