package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.kyber

import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.Kyber
import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.KyberAlgorithmOperationSelector
import org.sollecitom.chassis.cryptography.domain.asymmetric.KEMPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.KEMPublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.Algorithms
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMPrivateKeyFactory
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMPublicKeyFactory
import java.security.SecureRandom

// TODO merge this with Kyber?
internal class KyberAlgorithmOperationCustomizer(private val random: SecureRandom) : KyberAlgorithmOperationSelector {

    override val keyPair: KeyPairFactory<Kyber.KeyPairArguments, KEMPrivateKey, KEMPublicKey> by lazy { KyberKeyPairFactory(random) }
    override val privateKey: PrivateKeyFactory<KEMPrivateKey> by lazy { KEMPrivateKeyFactory(Algorithms.Kyber.NAME, random) }
    override val publicKey: PublicKeyFactory<KEMPublicKey> by lazy { KEMPublicKeyFactory(Algorithms.Kyber.NAME, random) }
}