package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem

import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory
import java.security.SecureRandom

internal class KEMPublicKeyFactory(private val algorithm: String, private val random: SecureRandom) : PublicKeyFactory<KEMPublicKey> {

    override fun fromBytes(bytes: ByteArray): KEMPublicKey = JavaKEMPublicKeyAdapter.fromBytes(bytes, algorithm, random)
}