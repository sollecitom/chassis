package org.sollecitom.chassis.cryptography.domain.asymmetric.factory

import org.sollecitom.chassis.cryptography.domain.asymmetric.PublicKey

interface PublicKeyFactory<PUBLIC : PublicKey> {

    fun fromBytes(bytes: ByteArray): PUBLIC
}