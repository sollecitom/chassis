package com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PublicKey

interface KeyPairFactory<in ARGUMENTS, PRIVATE : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PrivateKey, PUBLIC : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PublicKey> {

    operator fun invoke(arguments: ARGUMENTS): com.element.dpg.libs.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair<PRIVATE, PUBLIC>

    fun from(privateKey: PRIVATE, publicKey: PUBLIC): com.element.dpg.libs.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair<PRIVATE, PUBLIC>
}