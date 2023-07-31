package org.sollecitom.chassis.cryptography.domain.asymmetric

import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKey

interface PrivateKey : AsymmetricKey {

    fun decryptEncapsulatedAESKey(encapsulatedKey: ByteArray): SymmetricKey
}