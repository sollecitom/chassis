package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.signing

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import java.security.SecureRandom

internal class VerifyingPublicKeyFactory(private val algorithm: String, private val random: SecureRandom) : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory<VerifyingPublicKey> {

    override fun from(bytes: ByteArray): VerifyingPublicKey = JavaVerifyingPublicKeyAdapter.from(bytes, algorithm, random)
}