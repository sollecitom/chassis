package org.sollecitom.chassis.cryptography.domain.asymmetric.signing

import org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey
import java.nio.charset.Charset

interface SigningPrivateKey<OPTIONS> : PrivateKey {

    fun sign(input: ByteArray, options: OPTIONS): Signature
}

fun <OPTIONS> SigningPrivateKey<OPTIONS>.sign(input: String, options: OPTIONS, charset: Charset = Charsets.UTF_8) = sign(input.toByteArray(charset), options)

fun SigningPrivateKey<Unit>.sign(input: ByteArray) = sign(input, Unit)

fun SigningPrivateKey<Unit>.sign(input: String, charset: Charset = Charsets.UTF_8) = sign(input.toByteArray(charset))