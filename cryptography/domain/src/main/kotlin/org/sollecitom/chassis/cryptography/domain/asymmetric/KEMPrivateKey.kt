package org.sollecitom.chassis.cryptography.domain.asymmetric

import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKey

interface KEMPrivateKey : PrivateKey {

    fun decryptEncapsulatedAESKey(encapsulatedKey: ByteArray): SymmetricKey
}