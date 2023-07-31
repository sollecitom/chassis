package org.sollecitom.chassis.cryptography.domain.asymmetric.factory

import org.sollecitom.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair
import org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.PublicKey

interface KeyPairFactory<ARGUMENTS, PUBLIC : PublicKey> {

    operator fun invoke(arguments: ARGUMENTS): AsymmetricKeyPair<PUBLIC>

    fun fromKeys(publicKey: PUBLIC, privateKey: PrivateKey): AsymmetricKeyPair<PUBLIC>
}