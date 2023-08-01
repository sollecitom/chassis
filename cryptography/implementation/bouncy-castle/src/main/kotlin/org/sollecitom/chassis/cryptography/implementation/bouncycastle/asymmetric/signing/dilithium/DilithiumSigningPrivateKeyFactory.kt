package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.dilithium

import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import java.security.SecureRandom

// TODO make this generic by passing an algorithm to it
internal class DilithiumSigningPrivateKeyFactory(private val random: SecureRandom) : PrivateKeyFactory<SigningPrivateKey<Unit>> {

    override fun fromBytes(bytes: ByteArray): SigningPrivateKey<Unit> = JavaDilithiumPrivateKeyAdapter.fromBytes(bytes, random)
}