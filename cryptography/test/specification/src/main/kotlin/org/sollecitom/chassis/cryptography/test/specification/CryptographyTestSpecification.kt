package org.sollecitom.chassis.cryptography.test.specification

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.KyberKeyPairArguments
import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.KyberKeyPairArguments.Variant.KYBER_1024
import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.KyberKeyPairArguments.Variant.KYBER_1024_AES
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.factory.CryptographyCategorySelector
import org.sollecitom.chassis.cryptography.domain.symmetric.decrypt

@Suppress("FunctionName")
interface CryptographyTestSpecification { // TODO refactor this whole mess

    @Test
    fun `using an asymmetric key pair to generate and exchange a symmetric key securely`() {

        val factory = newCryptographyKeysFactory()
        // Bob
        val bobKeyPair = factory.asymmetric.crystals.kyber.keyPair()

        // Alice
        val decodedBobPublicKey = factory.asymmetric.crystals.kyber.publicKey.fromBytes(bobKeyPair.public.encoded) // receives Bob's public key
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
        assertThat(aliceSymmetricKey).isEqualTo(bobSymmetricKey)
    }

    // TODO crystals dilithium
    // TODO 1 party, with 1 identity, and a number of CertificateAndKeyPair (a keypair, and a certificate binding the identity to the public key)

    fun newCryptographyKeysFactory(): CryptographyCategorySelector
}

private val defaultKyberKeyPairArguments = KyberKeyPairArguments(variant = KYBER_1024_AES)

private operator fun KeyPairFactory<KyberKeyPairArguments>.invoke() = invoke(defaultKyberKeyPairArguments)
