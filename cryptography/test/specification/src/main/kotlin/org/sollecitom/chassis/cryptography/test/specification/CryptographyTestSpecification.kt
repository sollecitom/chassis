package org.sollecitom.chassis.cryptography.test.specification

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.dilithium.Dilithium
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.kyber.Kyber
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.verify
import org.sollecitom.chassis.cryptography.domain.factory.CryptographicOperations
import org.sollecitom.chassis.cryptography.domain.symmetric.algorithms.aes.AES
import org.sollecitom.chassis.cryptography.domain.symmetric.decrypt


@Suppress("FunctionName")
interface CryptographyTestSpecification {

    @Test
    fun `using Kyber to generate and exchange a symmetric key securely`() {

        // Bob
        // has a public key
        val bobKeyPair = kyber.keyPair(defaultKyberKeyPairArguments) // sends his public key to Alice

        // Alice
        val decodedBobPublicKey = kyber.publicKey.fromBytes(bobKeyPair.public.encoded) // receives Bob's public key
        val (aliceSymmetricKey, encapsulation) = decodedBobPublicKey.generateEncapsulatedAESKey() // generated encryption key with encapsulation
        // sends the encapsulation to Bob

        // Bob
        // receives the encapsulation from Alice
        val bobSymmetricKey = bobKeyPair.private.decryptEncapsulatedAESKey(encapsulation) // decrypts the encapsulated key

        // Alice
        // after the handshake is completed
        val aliceMessage = "a message".toByteArray() // prepares a message for Bob
        val encryptedByAlice = aliceSymmetricKey.ctr.encryptWithRandomIV(aliceMessage) // encrypts the message using the symmetric key and sends it to Bob

        // Bob
        // receives the encrypted data from Alice
        val decryptedByBobMessage = bobSymmetricKey.ctr.decrypt(encryptedByAlice) // decrypts the message
        assertThat(decryptedByBobMessage).isEqualTo(aliceMessage)

        assertThat(aliceSymmetricKey::encoded).isEqualTo(bobSymmetricKey.encoded)
        assertThat(aliceSymmetricKey::algorithm).isEqualTo(AES.name)
        assertThat(aliceSymmetricKey).isEqualTo(bobSymmetricKey)
    }

    @Test
    fun `sending Kyber keys over the wire`() {

        val keyPair = kyber.keyPair(arguments = Kyber.KeyPairArguments(variant = Kyber.Variant.KYBER_1024_AES))

        val decodedPublicKey = kyber.publicKey.fromBytes(keyPair.public.encoded)
        val decodedPrivateKey = kyber.privateKey.fromBytes(keyPair.private.encoded)

        assertThat(keyPair.private::algorithm).isEqualTo(Kyber.Variant.KYBER_1024_AES.algorithmName)
        assertThat(keyPair.public::algorithm).isEqualTo(Kyber.Variant.KYBER_1024_AES.algorithmName)
        assertThat(decodedPrivateKey).isEqualTo(keyPair.private)
        assertThat(decodedPublicKey).isEqualTo(keyPair.public)
    }

    @Test
    fun `using Dilithium to sign and verify`() {

        val keyPair = dilithium.keyPair(defaultDilithiumKeyPairArguments)
        val message = "something to attest".toByteArray()

        val signature = keyPair.private.sign(message)
        val verifies = keyPair.public.verify(message, signature)

        assertThat(verifies).isTrue()
        assertThat(signature.metadata::keyHash).isEqualTo(keyPair.private.hash)
        assertThat(signature.metadata::algorithmName).isEqualTo(keyPair.private.algorithm)

        val notTheOriginalSigner = dilithium.keyPair(defaultDilithiumKeyPairArguments).public

        assertThat(notTheOriginalSigner.verify(message, signature)).isFalse()
    }

    @Test
    fun `sending Dilithium keys over the wire`() {

        val keyPair = dilithium.keyPair(arguments = Dilithium.KeyPairArguments(variant = Dilithium.Variant.DILITHIUM_5_AES))

        val decodedPublicKey = dilithium.publicKey.fromBytes(keyPair.public.encoded)
        val decodedPrivateKey = dilithium.privateKey.fromBytes(keyPair.private.encoded)

        assertThat(keyPair.private::algorithm).isEqualTo(Dilithium.Variant.DILITHIUM_5_AES.value)
        assertThat(keyPair.public::algorithm).isEqualTo(Dilithium.Variant.DILITHIUM_5_AES.value)
        assertThat(decodedPrivateKey).isEqualTo(keyPair.private)
        assertThat(decodedPublicKey).isEqualTo(keyPair.public)
    }


    val cryptography: CryptographicOperations
    val kyber get() = cryptography.asymmetric.crystals.kyber
    val dilithium get() = cryptography.asymmetric.crystals.dilithium

    // TODO 1 party, with 1 identity, and a number of CertificateAndKeyPair (a keypair, and a certificate binding the identity to the public key)
}

private val defaultKyberKeyPairArguments = Kyber.KeyPairArguments(variant = Kyber.Variant.KYBER_1024_AES)

private val defaultDilithiumKeyPairArguments = Dilithium.KeyPairArguments(variant = Dilithium.Variant.DILITHIUM_5_AES)