package com.element.dpg.libs.chassis.cryptography.domain.symmetric

interface SecretKeyFactory<in ARGUMENTS, out KEY : SymmetricKey> {

    operator fun invoke(arguments: ARGUMENTS): KEY

    fun from(bytes: ByteArray): KEY
}