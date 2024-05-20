package com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PrivateKey

interface PrivateKeyFactory<out PRIVATE : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PrivateKey> {

    fun from(bytes: ByteArray): PRIVATE
}