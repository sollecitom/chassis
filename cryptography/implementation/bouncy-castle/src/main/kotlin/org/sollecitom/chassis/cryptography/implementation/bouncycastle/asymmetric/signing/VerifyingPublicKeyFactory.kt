package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing

import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import java.security.SecureRandom

internal class VerifyingPublicKeyFactory(private val algorithm: String, private val random: SecureRandom) : PublicKeyFactory<VerifyingPublicKey> {

    override fun from(bytes: ByteArray): VerifyingPublicKey = JavaVerifyingPublicKeyAdapter.from(bytes, algorithm, random)
}