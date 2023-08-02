package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing

import org.sollecitom.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair
import org.sollecitom.chassis.cryptography.domain.asymmetric.KeyPair
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec
import java.security.KeyPair as JavaKeyPair

internal class SigningKeyPairFactory<ARGUMENTS>(private val algorithm: String, private val random: SecureRandom, private val spec: ARGUMENTS.() -> AlgorithmParameterSpec) : KeyPairFactory<ARGUMENTS, SigningPrivateKey, VerifyingPublicKey> {

    override fun invoke(arguments: ARGUMENTS): AsymmetricKeyPair<SigningPrivateKey, VerifyingPublicKey> = arguments.generateRawKeyPair().asSigningAndVerifyingPrivateKey(random)

    override fun from(privateKey: SigningPrivateKey, publicKey: VerifyingPublicKey): AsymmetricKeyPair<SigningPrivateKey, VerifyingPublicKey> {

        require(privateKey.algorithm == algorithm) { "Private key algorithm must be $algorithm" }
        require(publicKey.algorithm == algorithm) { "Public key algorithm must be $algorithm" }
        return KeyPair(privateKey, publicKey)
    }

    private fun ARGUMENTS.generateRawKeyPair(): JavaKeyPair = BouncyCastleUtils.generateKeyPair(algorithm, spec(), random)

    private fun JavaKeyPair.asSigningAndVerifyingPrivateKey(random: SecureRandom) = KeyPair(private = private.asSigningPrivateKey(random), public = public.asVerifyingPublicKey(random))

    private fun PrivateKey.asSigningPrivateKey(random: SecureRandom): SigningPrivateKey = JavaSigningKeyAdapter(this, random)

    private fun PublicKey.asVerifyingPublicKey(random: SecureRandom): VerifyingPublicKey = JavaVerifyingPublicKeyAdapter(this, random)
}