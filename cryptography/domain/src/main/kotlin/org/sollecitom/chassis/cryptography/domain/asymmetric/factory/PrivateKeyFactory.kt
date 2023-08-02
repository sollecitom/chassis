package org.sollecitom.chassis.cryptography.domain.asymmetric.factory

import org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey

interface PrivateKeyFactory<out PRIVATE : PrivateKey> {

    fun from(bytes: ByteArray): PRIVATE
}