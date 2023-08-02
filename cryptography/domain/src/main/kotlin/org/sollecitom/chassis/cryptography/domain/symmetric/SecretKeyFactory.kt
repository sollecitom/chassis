package org.sollecitom.chassis.cryptography.domain.symmetric

interface SecretKeyFactory<in ARGUMENTS, out KEY : SymmetricKey> {

    operator fun invoke(arguments: ARGUMENTS): KEY

    fun fromBytes(bytes: ByteArray): KEY
}