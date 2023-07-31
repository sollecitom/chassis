package org.sollecitom.chassis.cryptography.domain.asymmetric.factory

import org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey

interface PrivateKeyFactory {

    fun fromBytes(bytes: ByteArray): PrivateKey
}