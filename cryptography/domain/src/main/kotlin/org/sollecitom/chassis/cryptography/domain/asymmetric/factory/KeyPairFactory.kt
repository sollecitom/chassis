package org.sollecitom.chassis.cryptography.domain.asymmetric.factory

import org.sollecitom.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair
import org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.PublicKey

interface KeyPairFactory<ARGUMENTS> {

    operator fun invoke(arguments: ARGUMENTS): AsymmetricKeyPair // TODO needs generics for signature metadata

    fun fromKeys(publicKey: PublicKey, privateKey: PrivateKey): AsymmetricKeyPair // TODO needs generics for signature metadata
}