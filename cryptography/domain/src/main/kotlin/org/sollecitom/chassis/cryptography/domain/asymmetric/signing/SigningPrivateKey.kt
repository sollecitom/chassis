package org.sollecitom.chassis.cryptography.domain.asymmetric.signing

import org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey
import java.nio.charset.Charset

interface SigningPrivateKey : PrivateKey {

    fun sign(input: ByteArray): Signature
}

fun <OPTIONS> SigningPrivateKey.sign(input: String, charset: Charset = Charsets.UTF_8) = sign(input.toByteArray(charset))