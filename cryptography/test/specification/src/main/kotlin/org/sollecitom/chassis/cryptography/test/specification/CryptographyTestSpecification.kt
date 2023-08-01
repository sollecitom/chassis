package org.sollecitom.chassis.cryptography.test.specification

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.kyber.Kyber
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import org.sollecitom.chassis.cryptography.domain.factory.CryptographicOperations
import org.sollecitom.chassis.cryptography.domain.symmetric.algorithms.aes.AES
import org.sollecitom.chassis.cryptography.domain.symmetric.decrypt


@Suppress("FunctionName")
interface CryptographyTestSpecification {

    @Test
    fun `using Kyber to generate and exchange a symmetric key securely`() {

        // Bob
        // has a public key
        val bobKeyPair = crystalsKyber.keyPair() // sends his public key to Alice

        // Alice
        val decodedBobPublicKey = crystalsKyber.publicKey.fromBytes(bobKeyPair.public.encoded) // receives Bob's public key
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

        assertThat(aliceSymmetricKey.encoded).isEqualTo(bobSymmetricKey.encoded)
        assertThat(aliceSymmetricKey.algorithm).isEqualTo(AES.NAME)
        assertThat(aliceSymmetricKey).isEqualTo(bobSymmetricKey)
    }

    @Test
    fun `sending Kyber keys over the wire`() {

        val keyPair = crystalsKyber.keyPair(arguments = Kyber.KeyPairArguments(variant = Kyber.Variant.KYBER_1024_AES))

        val decodedPublicKey = crystalsKyber.publicKey.fromBytes(keyPair.public.encoded)
        val decodedPrivateKey = crystalsKyber.privateKey.fromBytes(keyPair.private.encoded)

        assertThat(keyPair.private.algorithm).isEqualTo(Kyber.Variant.KYBER_1024_AES.algorithmName)
        assertThat(keyPair.public.algorithm).isEqualTo(Kyber.Variant.KYBER_1024_AES.algorithmName)
        assertThat(decodedPrivateKey).isEqualTo(keyPair.private)
        assertThat(decodedPublicKey).isEqualTo(keyPair.public)
    }


    val cryptography: CryptographicOperations
    val crystalsKyber get() = cryptography.asymmetric.crystals.kyber

    // TODO crystals dilithium
    // TODO 1 party, with 1 identity, and a number of CertificateAndKeyPair (a keypair, and a certificate binding the identity to the public key)
}

private val defaultKyberKeyPairArguments = Kyber.KeyPairArguments(variant = Kyber.Variant.KYBER_1024_AES)

private operator fun KeyPairFactory<Kyber.KeyPairArguments, KEMPrivateKey, KEMPublicKey>.invoke() = invoke(defaultKyberKeyPairArguments)
