package org.sollecitom.chassis.cryptography.implementation.bouncycastle

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.pqc.jcajce.spec.DilithiumParameterSpec
import org.bouncycastle.util.Strings
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.cryptography.domain.factory.CryptographicOperations
import org.sollecitom.chassis.cryptography.test.specification.CryptographyTestSpecification
import java.security.*


@TestInstance(PER_CLASS)
private class BouncyCastleCryptographyExampleTests : CryptographyTestSpecification {

    override val cryptography: CryptographicOperations get() = CryptographicOperations.bouncyCastle()

    // TODO remove
    init {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(BouncyCastleProvider())
        }
    }

    @Test // TODO remove after migrating this to the spec with wrappers
    fun `using Dilithium to sign and verify`() {

        val msg: ByteArray = Strings.toByteArray("Hello World!")

        val sigAlgorithm = "DILITHIUM5-AES"
        val spec = DilithiumParameterSpec.dilithium5_aes
        val kpg = KeyPairGenerator.getInstance("Dilithium", "BC")

        kpg.initialize(spec, SecureRandom())

        val kp: KeyPair = kpg.generateKeyPair()

        var sig: Signature = Signature.getInstance(sigAlgorithm, "BC")

        sig.initSign(kp.private, SecureRandom())

        sig.update(msg, 0, msg.size)

        val s: ByteArray = sig.sign()

        sig = Signature.getInstance(sigAlgorithm, "BC")

        assertThat(sigAlgorithm).isEqualTo(sig.algorithm)

        sig.initVerify(kp.public)

        sig.update(msg, 0, msg.size)

        assertThat(sig.verify(s)).isTrue()
    }
}