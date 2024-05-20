package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.kyber

import org.bouncycastle.pqc.jcajce.spec.KyberParameterSpec
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.KeyPairGenerationOperations
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMAlgorithm
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMKeyPairFactory
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMPrivateKeyFactory
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMPublicKeyFactory
import java.security.SecureRandom

object Kyber : KEMAlgorithm<_root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.KeyPairArguments> {

    override val name: String get() = _root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.name

    override fun keyPairGenerationOperations(random: SecureRandom): com.element.dpg.libs.chassis.cryptography.domain.asymmetric.KeyPairGenerationOperations<_root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.KeyPairArguments, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey> = com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.kyber.KyberAlgorithmOperationCustomizer(random)
}

private class KyberAlgorithmOperationCustomizer(private val random: SecureRandom) : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.KeyPairGenerationOperations<_root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.KeyPairArguments, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey> {

    override val keyPair by lazy { KEMKeyPairFactory<_root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.KeyPairArguments>(_root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.name, random) { variant.spec } }
    override val privateKey by lazy { KEMPrivateKeyFactory(_root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.name, random) }
    override val publicKey by lazy { KEMPublicKeyFactory(_root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.name, random) }

    private val _root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.Variant.spec: KyberParameterSpec
        get() = when (this) {
            _root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.Variant.KYBER_512 -> KyberParameterSpec.kyber512
            _root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.Variant.KYBER_768 -> KyberParameterSpec.kyber768
            _root_ide_package_.com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber.Variant.KYBER_1024 -> KyberParameterSpec.kyber1024
        }
}