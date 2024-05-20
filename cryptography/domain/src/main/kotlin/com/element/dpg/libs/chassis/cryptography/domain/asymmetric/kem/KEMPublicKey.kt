package com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem

import com.element.dpg.libs.chassis.cryptography.domain.symmetric.SymmetricKeyWithEncapsulation

interface KEMPublicKey : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PublicKey {

    fun generateEncapsulatedAESKey(): SymmetricKeyWithEncapsulation
}