package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.dilithium

import org.bouncycastle.pqc.jcajce.spec.DilithiumParameterSpec
import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.KeyPairGenerationOperations
import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.dilithium.Dilithium
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.*
import java.security.SecureRandom

object Dilithium : SigningAlgorithm<Dilithium.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey> {

    override val name: String get() = Dilithium.name

    override fun keyPairGenerationOperations(random: SecureRandom): KeyPairGenerationOperations<Dilithium.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey> = DilithiumAlgorithmOperationCustomizer(random)
}

private class DilithiumAlgorithmOperationCustomizer(private val random: SecureRandom) : KeyPairGenerationOperations<Dilithium.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey> {

    override val keyPair by lazy { SigningKeyPairFactory<Dilithium.KeyPairArguments>(Dilithium.name, random) { variant.spec } }
    override val privateKey by lazy { SigningPrivateKeyFactory(Dilithium.name, random) }
    override val publicKey by lazy { VerifyingPublicKeyFactory(Dilithium.name, random) }

    private val Dilithium.Variant.spec: DilithiumParameterSpec
        get() = when (this) {
            Dilithium.Variant.DILITHIUM_2 -> DilithiumParameterSpec.dilithium2
            Dilithium.Variant.DILITHIUM_3 -> DilithiumParameterSpec.dilithium3
            Dilithium.Variant.DILITHIUM_5 -> DilithiumParameterSpec.dilithium5
            Dilithium.Variant.DILITHIUM_2_AES -> DilithiumParameterSpec.dilithium2_aes
            Dilithium.Variant.DILITHIUM_3_AES -> DilithiumParameterSpec.dilithium3_aes
            Dilithium.Variant.DILITHIUM_5_AES -> DilithiumParameterSpec.dilithium5_aes
        }
}

