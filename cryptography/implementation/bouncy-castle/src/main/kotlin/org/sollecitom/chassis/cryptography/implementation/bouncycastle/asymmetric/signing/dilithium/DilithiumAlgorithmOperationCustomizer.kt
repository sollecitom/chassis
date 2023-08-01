package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.dilithium

import org.sollecitom.chassis.cryptography.domain.algorithms.dilithium.Dilithium
import org.sollecitom.chassis.cryptography.domain.algorithms.dilithium.DilithiumAlgorithmOperationSelector
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.VerifyingPublicKeyFactory
import java.security.SecureRandom

// TODO merge this with Dilithium?
internal class DilithiumAlgorithmOperationCustomizer(private val random: SecureRandom) : DilithiumAlgorithmOperationSelector {

    override val keyPair: KeyPairFactory<Dilithium.KeyPairArguments, SigningPrivateKey<Unit>, VerifyingPublicKey> by lazy { DilithiumKeyPairFactory(random) }
    override val privateKey: PrivateKeyFactory<SigningPrivateKey<Unit>> by lazy { DilithiumSigningPrivateKeyFactory(random) }
    override val publicKey: PublicKeyFactory<VerifyingPublicKey> by lazy { VerifyingPublicKeyFactory(Dilithium.NAME, random) }
}