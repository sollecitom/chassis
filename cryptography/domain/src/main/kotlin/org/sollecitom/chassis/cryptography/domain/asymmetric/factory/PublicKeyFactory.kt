package org.sollecitom.chassis.cryptography.domain.asymmetric.factory

import org.sollecitom.chassis.cryptography.domain.asymmetric.PublicKey

interface PublicKeyFactory<out PUBLIC : PublicKey> {

    fun fromBytes(bytes: ByteArray): PUBLIC
}