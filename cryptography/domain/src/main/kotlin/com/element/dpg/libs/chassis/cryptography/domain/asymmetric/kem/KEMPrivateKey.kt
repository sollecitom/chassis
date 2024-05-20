package com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem

import com.element.dpg.libs.chassis.cryptography.domain.symmetric.SymmetricKey

interface KEMPrivateKey : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PrivateKey {

    fun decryptEncapsulatedAESKey(encapsulatedKey: ByteArray): SymmetricKey
}