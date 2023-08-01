package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.kyber

import org.bouncycastle.pqc.jcajce.spec.KyberParameterSpec
import org.sollecitom.chassis.cryptography.domain.asymmetric.KeyPairGenerationOperations
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMAlgorithm
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMKeyPairFactory
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMPrivateKeyFactory
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMPublicKeyFactory
import java.security.SecureRandom

object Kyber : KEMAlgorithm<Kyber.KeyPairArguments> {

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