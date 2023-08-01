package org.sollecitom.chassis.cryptography.domain.asymmetric.factory

import org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey

interface PrivateKeyFactory<out PRIVATE : PrivateKey> {

    fun fromBytes(bytes: ByteArray): PRIVATE
}