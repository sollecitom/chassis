package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing

import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import java.security.SecureRandom

internal class SigningPrivateKeyFactory(private val algorithm: String, private val random: SecureRandom) : PrivateKeyFactory<SigningPrivateKey> {

    override fun fromBytes(bytes: ByteArray): SigningPrivateKey = JavaSigningKeyAdapter.fromBytes(bytes, random, algorithm)
}