package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.dilithium

import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.KeyPairGenerationOperations
import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.dilithium.Dilithium
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.SigningPrivateKeyFactory
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.VerifyingPublicKeyFactory
import java.security.SecureRandom

// TODO merge this with Dilithium?
internal class DilithiumAlgorithmOperationCustomizer(private val random: SecureRandom) : KeyPairGenerationOperations<Dilithium.KeyPairArguments, SigningPrivateKey, VerifyingPublicKey> {

    override val keyPair by lazy { DilithiumKeyPairFactory(random) }
    override val privateKey by lazy { SigningPrivateKeyFactory(Dilithium.name, random) }
    override val publicKey by lazy { VerifyingPublicKeyFactory(Dilithium.name, random) }
}