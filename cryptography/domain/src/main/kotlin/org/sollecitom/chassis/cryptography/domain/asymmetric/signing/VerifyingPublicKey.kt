package org.sollecitom.chassis.cryptography.domain.asymmetric.signing

import org.sollecitom.chassis.cryptography.domain.asymmetric.PublicKey
import java.nio.charset.Charset

interface VerifyingPublicKey : PublicKey {

    fun verify(input: ByteArray, signatureBytes: ByteArray, signatureAlgorithm: String): Boolean
}

fun VerifyingPublicKey.verify(input: String, signatureBytes: ByteArray, signatureHashAlgorithm: String, charset: Charset = Charsets.UTF_8): Boolean = verify(input.toByteArray(charset), signatureBytes, signatureHashAlgorithm)

fun VerifyingPublicKey.verify(input: String, signature: Signature, charset: Charset = Charsets.UTF_8): Boolean = verify(input.toByteArray(charset), signature.bytes, signature.metadata.algorithmName)

fun VerifyingPublicKey.verify(input: ByteArray, signature: Signature): Boolean = verify(input, signature.bytes, signature.metadata.algorithmName)