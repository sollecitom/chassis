package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric

import org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import java.security.SecureRandom

internal class GenericPrivateKeyFactory(private val algorithm: String, private val random: SecureRandom) : PrivateKeyFactory {

    override fun fromBytes(bytes: ByteArray): PrivateKey = JavaPrivateKeyAdapter.fromBytes(bytes, algorithm, random)
}