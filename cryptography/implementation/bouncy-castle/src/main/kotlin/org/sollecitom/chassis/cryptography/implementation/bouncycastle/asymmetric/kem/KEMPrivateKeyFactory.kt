package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem

import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import java.security.SecureRandom

internal class KEMPrivateKeyFactory(private val algorithm: String, private val random: SecureRandom) : PrivateKeyFactory<KEMPrivateKey> {

    override fun fromBytes(bytes: ByteArray): KEMPrivateKey = JavaKEMPrivateKeyAdapter.fromBytes(bytes, algorithm, random)
}