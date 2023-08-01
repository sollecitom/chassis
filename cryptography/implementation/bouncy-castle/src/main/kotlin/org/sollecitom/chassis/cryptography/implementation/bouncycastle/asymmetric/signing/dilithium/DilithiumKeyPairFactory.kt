package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.dilithium

import org.bouncycastle.pqc.jcajce.spec.DilithiumParameterSpec
import org.sollecitom.chassis.cryptography.domain.asymmetric.*
import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.dilithium.Dilithium
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.JavaVerifyingPublicKeyAdapter
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.utils.BouncyCastleUtils
import java.security.PublicKey
import java.security.SecureRandom
import java.security.KeyPair as JavaKeyPair
import java.security.PrivateKey as JavaPrivateKey

internal class DilithiumKeyPairFactory(private val random: SecureRandom) : KeyPairFactory<Dilithium.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey> {

    override fun invoke(arguments: Dilithium.KeyPairArguments): AsymmetricKeyPair<SigningPrivateKey, VerifyingPublicKey> = arguments.generateRawKeyPair().asSigningAndVerifyingPrivateKey(random)

    override fun fromKeys(privateKey: SigningPrivateKey, publicKey: VerifyingPublicKey): AsymmetricKeyPair<SigningPrivateKey, VerifyingPublicKey> {

        require(privateKey.algorithm == Dilithium.name) { "Private key algorithm must be ${Dilithium.name}" }
        require(publicKey.algorithm == Dilithium.name) { "Public key algorithm must be ${Dilithium.name}" }
        return KeyPair(privateKey, publicKey)
    }

    private val Dilithium.Variant.spec: DilithiumParameterSpec
        get() = when (this) {
            Dilithium.Variant.DILITHIUM_2 -> DilithiumParameterSpec.dilithium2
            Dilithium.Variant.DILITHIUM_3 -> DilithiumParameterSpec.dilithium3
            Dilithium.Variant.DILITHIUM_5 -> DilithiumParameterSpec.dilithium5
            Dilithium.Variant.DILITHIUM_2_AES -> DilithiumParameterSpec.dilithium2_aes
            Dilithium.Variant.DILITHIUM_3_AES -> DilithiumParameterSpec.dilithium3_aes
            Dilithium.Variant.DILITHIUM_5_AES -> DilithiumParameterSpec.dilithium5_aes
        }

    private fun Dilithium.Variant.generateRawKeyPair(): JavaKeyPair = BouncyCastleUtils.generateKeyPair(Dilithium.name, spec, random)

    private fun Dilithium.KeyPairArguments.generateRawKeyPair() = variant.generateRawKeyPair()

    private fun JavaKeyPair.asSigningAndVerifyingPrivateKey(random: SecureRandom) = KeyPair(private = private.asSigningPrivateKey(random), public = public.asVerifyingPublicKey(random))

    private fun JavaPrivateKey.asSigningPrivateKey(random: SecureRandom): SigningPrivateKey = JavaDilithiumPrivateKeyAdapter(this, random) // TODO make this whole class SigningKeyPairFactory after JavaDilithiumPrivateKeyAdapter will have become JavaSigningPrivateKeyAdapter
    private fun PublicKey.asVerifyingPublicKey(random: SecureRandom): VerifyingPublicKey = JavaVerifyingPublicKeyAdapter(this, random)
}