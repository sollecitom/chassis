package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.kem

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import java.security.SecureRandom

internal class KEMPrivateKeyFactory(private val algorithm: String, private val random: SecureRandom) : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory<com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey> {

    override fun from(bytes: ByteArray): com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey = JavaKEMPrivateKeyAdapter.from(bytes, algorithm, random)
}