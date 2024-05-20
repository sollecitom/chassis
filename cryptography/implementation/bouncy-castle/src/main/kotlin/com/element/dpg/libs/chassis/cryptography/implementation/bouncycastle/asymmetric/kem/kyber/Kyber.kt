package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.kyber

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMAlgorithm
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMKeyPairFactory
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMPrivateKeyFactory
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMPublicKeyFactory
import org.bouncycastle.pqc.jcajce.spec.KyberParameterSpec
import java.security.SecureRandom

object Kyber : KEMAlgorithm<Kyber.KeyPairArguments> {

    override val name: String get() = Kyber.name

    override fun keyPairGenerationOperations(random: SecureRandom): com.element.dpg.libs.chassis.cryptography.domain.asymmetric.KeyPairGenerationOperations<Kyber.KeyPairArguments, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey> = KyberAlgorithmOperationCustomizer(random)
}

private class KyberAlgorithmOperationCustomizer(private val random: SecureRandom) : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.KeyPairGenerationOperations<Kyber.KeyPairArguments, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey> {

    override val keyPair by lazy { KEMKeyPairFactory<Kyber.KeyPairArguments>(Kyber.name, random) { variant.spec } }
    override val privateKey by lazy { KEMPrivateKeyFactory(Kyber.name, random) }
    override val publicKey by lazy { KEMPublicKeyFactory(Kyber.name, random) }

    private val Kyber.Variant.spec: KyberParameterSpec
        get() = when (this) {
            Kyber.Variant.KYBER_512 -> KyberParameterSpec.kyber512
            Kyber.Variant.KYBER_768 -> KyberParameterSpec.kyber768
            Kyber.Variant.KYBER_1024 -> KyberParameterSpec.kyber1024
        }
}