package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.signing

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import java.security.SecureRandom

internal class SigningPrivateKeyFactory(private val algorithm: String, private val random: SecureRandom) : PrivateKeyFactory<SigningPrivateKey> {

    override fun from(bytes: ByteArray): SigningPrivateKey = JavaSigningKeyAdapter.from(bytes, random, algorithm)
}