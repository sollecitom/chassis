package org.sollecitom.chassis.cryptography.domain.asymmetric

import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKeyWithEncapsulation

interface PublicKey : AsymmetricKey {

    fun generateEncapsulatedAESKey(): SymmetricKeyWithEncapsulation
}