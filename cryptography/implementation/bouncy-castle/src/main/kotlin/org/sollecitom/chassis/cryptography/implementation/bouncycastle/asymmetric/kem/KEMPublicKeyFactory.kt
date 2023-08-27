package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem

import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import java.security.SecureRandom

internal class KEMPublicKeyFactory(private val algorithm: String, private val random: SecureRandom) : PublicKeyFactory<KEMPublicKey> {

    override fun from(bytes: ByteArray): KEMPublicKey = JavaKEMPublicKeyAdapter.from(bytes, algorithm, random)
}