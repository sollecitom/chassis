package org.sollecitom.chassis.cryptography.domain.asymmetric.kem

import org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKey

interface KEMPrivateKey : PrivateKey {

    fun decryptEncapsulatedAESKey(encapsulatedKey: ByteArray): SymmetricKey
}