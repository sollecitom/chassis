package org.sollecitom.chassis.cryptography.domain.asymmetric

import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKeyWithEncapsulation

interface KEMPublicKey : PublicKey {

    fun generateEncapsulatedAESKey(): SymmetricKeyWithEncapsulation
}