package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem

import org.sollecitom.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec

internal class KEMKeyPairFactory<ARGUMENTS>(private val algorithm: String, private val random: SecureRandom, private val spec: ARGUMENTS.() -> AlgorithmParameterSpec) : KeyPairFactory<ARGUMENTS, KEMPrivateKey, KEMPublicKey> {

    override fun invoke(arguments: ARGUMENTS): AsymmetricKeyPair<KEMPrivateKey, KEMPublicKey> = arguments.generateRawKeyPair().asKEMKeyPair(random)

    override fun fromKeys(privateKey: KEMPrivateKey, publicKey: KEMPublicKey): AsymmetricKeyPair<KEMPrivateKey, KEMPublicKey> {

        require(privateKey.algorithm == algorithm) { "Private key algorithm must be $algorithm" }
        require(publicKey.algorithm == algorithm) { "Public key algorithm must be $algorithm" }
        return org.sollecitom.chassis.cryptography.domain.asymmetric.KeyPair(privateKey, publicKey)
    }

    private fun ARGUMENTS.generateRawKeyPair(): KeyPair = BouncyCastleUtils.generateKeyPair(algorithm, spec(), random)

    private fun KeyPair.asKEMKeyPair(random: SecureRandom) = org.sollecitom.chassis.cryptography.domain.asymmetric.KeyPair(private = private.asKEMPrivateKey(random), public = public.asKEMPublicKey(random))

    private fun PrivateKey.asKEMPrivateKey(random: SecureRandom): KEMPrivateKey = JavaKEMPrivateKeyAdapter(this, random)

    private fun PublicKey.asKEMPublicKey(random: SecureRandom): KEMPublicKey = JavaKEMPublicKeyAdapter(this, random)
}