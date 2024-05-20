package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.kem

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.AlgorithmParameterSpec

internal class KEMKeyPairFactory<ARGUMENTS>(private val algorithm: String, private val random: SecureRandom, private val spec: ARGUMENTS.() -> AlgorithmParameterSpec) : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory<ARGUMENTS, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey> {

    override fun invoke(arguments: ARGUMENTS): com.element.dpg.libs.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair<com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey> = arguments.generateRawKeyPair().asKEMKeyPair(random)

    override fun from(privateKey: com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey, publicKey: com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey): com.element.dpg.libs.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair<com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey> {

        require(privateKey.algorithm == algorithm) { "Private key algorithm must be $algorithm" }
        require(publicKey.algorithm == algorithm) { "Public key algorithm must be $algorithm" }
        return com.element.dpg.libs.chassis.cryptography.domain.asymmetric.KeyPair(privateKey, publicKey)
    }

    private fun ARGUMENTS.generateRawKeyPair(): KeyPair = BouncyCastleUtils.generateKeyPair(algorithm, spec(), random)

    private fun KeyPair.asKEMKeyPair(random: SecureRandom) = com.element.dpg.libs.chassis.cryptography.domain.asymmetric.KeyPair(private = private.asKEMPrivateKey(random), public = public.asKEMPublicKey(random))

    private fun PrivateKey.asKEMPrivateKey(random: SecureRandom): com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey = JavaKEMPrivateKeyAdapter(this, random)

    private fun PublicKey.asKEMPublicKey(random: SecureRandom): com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey = JavaKEMPublicKeyAdapter(this, random)
}