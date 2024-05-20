package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.dilithium

import org.bouncycastle.pqc.jcajce.spec.DilithiumParameterSpec
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.KeyPairGenerationOperations
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.dilithium.Dilithium
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.SigningAlgorithm
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.SigningKeyPairFactory
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.SigningPrivateKeyFactory
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.VerifyingPublicKeyFactory
import java.security.SecureRandom

object Dilithium : SigningAlgorithm<Dilithium.KeyPairArguments> {

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
        }
}

