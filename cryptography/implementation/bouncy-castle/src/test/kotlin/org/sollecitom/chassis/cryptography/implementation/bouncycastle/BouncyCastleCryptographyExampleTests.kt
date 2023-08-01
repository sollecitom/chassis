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
import org.sollecitom.chassis.cryptography.domain.algorithms.dilithium.Dilithium
import org.sollecitom.chassis.cryptography.domain.asymmetric.SigningPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.VerifyingPublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.factory.CryptographicOperations
import org.sollecitom.chassis.cryptography.domain.symmetric.decrypt
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
    fun `using Dilithium to sign and verify - raw`() {

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

    @Test
    fun `using Dilithium to sign and verify`() {

        val keyPair = crystalsDilithium.keyPair()

        val decodedPublicKey = crystalsDilithium.publicKey.fromBytes(keyPair.public.encoded)
        val decodedPrivateKey = crystalsDilithium.privateKey.fromBytes(keyPair.private.encoded)

//        assertThat()

//        val decodedBobPublicKey = crystalsKyber.publicKey.fromBytes(bobKeyPair.public.encoded) // receives Bob's public key
//        val (aliceSymmetricKey, encapsulation) = decodedBobPublicKey.generateEncapsulatedAESKey() // generated encryption key with encapsulation
//
//        val bobSymmetricKey = bobKeyPair.private.decryptEncapsulatedAESKey(encapsulation) // decrypts the encapsulated key
//
//        val aliceMessage = "a message".toByteArray() // prepares a message for Bob
//        val encryptedByAlice = aliceSymmetricKey.ctr.encryptWithRandomIV(aliceMessage) // encrypts the message using the symmetric key and sends it to Bob
//
//        val decryptedByBobMessage = bobSymmetricKey.ctr.decrypt(encryptedByAlice) // decrypts the message
//        assertThat(decryptedByBobMessage).isEqualTo(aliceMessage)
//
//        assertThat(aliceSymmetricKey.encoded).isEqualTo(bobSymmetricKey.encoded)
//        assertThat(aliceSymmetricKey).isEqualTo(bobSymmetricKey)
    }

    val crystalsDilithium get() = cryptography.asymmetric.crystals.dilithium
}

private val defaultDilithiumKeyPairArguments = Dilithium.KeyPairArguments(variant = Dilithium.Variant.DILITHIUM_5_AES)

private operator fun KeyPairFactory<Dilithium.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey>.invoke() = invoke(defaultDilithiumKeyPairArguments)