package org.sollecitom.chassis.cryptography.domain.asymmetric.signing.dilithium

import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.SigningAlgorithm
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey

object Dilithium : SigningAlgorithm<Dilithium.KeyPairArguments> {

    override val name = "Dilithium"

    enum class Variant(val value: String) {

        DILITHIUM_2("DILITHIUM2"),
        DILITHIUM_3("DILITHIUM3"),
        DILITHIUM_5("DILITHIUM5")
    }

    data class KeyPairArguments(val variant: Variant)
}

operator fun KeyPairFactory<Dilithium.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey>.invoke(variant: Dilithium.Variant) = invoke(arguments = Dilithium.KeyPairArguments(variant = variant))