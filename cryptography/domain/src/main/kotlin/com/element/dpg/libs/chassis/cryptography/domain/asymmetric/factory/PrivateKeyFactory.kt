package com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory

interface PrivateKeyFactory<out PRIVATE : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PrivateKey> {

    fun from(bytes: ByteArray): PRIVATE
}