package com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory

interface PublicKeyFactory<out PUBLIC : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PublicKey> {

    fun from(bytes: ByteArray): PUBLIC
}