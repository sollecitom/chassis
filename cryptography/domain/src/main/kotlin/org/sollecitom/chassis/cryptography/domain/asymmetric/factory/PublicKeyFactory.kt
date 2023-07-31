package org.sollecitom.chassis.cryptography.domain.asymmetric.factory

import org.sollecitom.chassis.cryptography.domain.asymmetric.PublicKey

interface PublicKeyFactory {

    fun fromBytes(bytes: ByteArray): PublicKey
}