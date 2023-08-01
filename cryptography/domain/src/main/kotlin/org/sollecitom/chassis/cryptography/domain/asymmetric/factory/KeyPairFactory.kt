package org.sollecitom.chassis.cryptography.domain.asymmetric.factory

import org.sollecitom.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair
import org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.PublicKey

interface KeyPairFactory<in ARGUMENTS, PRIVATE : PrivateKey, PUBLIC : PublicKey> {

    operator fun invoke(arguments: ARGUMENTS): AsymmetricKeyPair<PRIVATE, PUBLIC>

    fun fromKeys(publicKey: PUBLIC, privateKey: PRIVATE): AsymmetricKeyPair<PRIVATE, PUBLIC>
}