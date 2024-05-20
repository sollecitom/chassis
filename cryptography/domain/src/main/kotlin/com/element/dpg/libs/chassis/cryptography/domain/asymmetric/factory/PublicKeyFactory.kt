package com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PublicKey

interface PublicKeyFactory<out PUBLIC : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PublicKey> {

    fun from(bytes: ByteArray): PUBLIC
}