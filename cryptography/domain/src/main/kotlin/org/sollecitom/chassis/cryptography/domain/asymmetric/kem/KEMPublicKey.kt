package org.sollecitom.chassis.cryptography.domain.asymmetric.kem

import org.sollecitom.chassis.cryptography.domain.asymmetric.PublicKey
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKeyWithEncapsulation

interface KEMPublicKey : PublicKey {

    fun generateEncapsulatedAESKey(): SymmetricKeyWithEncapsulation
}