package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.kem

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import java.security.SecureRandom

internal class KEMPublicKeyFactory(private val algorithm: String, private val random: SecureRandom) : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory<com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey> {

    override fun from(bytes: ByteArray): com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey = JavaKEMPublicKeyAdapter.from(bytes, algorithm, random)
}