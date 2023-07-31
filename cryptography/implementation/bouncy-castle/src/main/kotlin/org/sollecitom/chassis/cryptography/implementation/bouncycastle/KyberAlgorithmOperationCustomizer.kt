package org.sollecitom.chassis.cryptography.implementation.bouncycastle

import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.KyberAlgorithmOperationSelector
import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.KyberKeyPairArguments
import org.sollecitom.chassis.cryptography.domain.asymmetric.KEMPublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory
import java.security.SecureRandom

internal class KyberAlgorithmOperationCustomizer(private val random: SecureRandom) : KyberAlgorithmOperationSelector {

    override val keyPair: KeyPairFactory<KyberKeyPairArguments, KEMPublicKey> by lazy { KyberKeyPairFactory(random) }
    override val privateKey: PrivateKeyFactory by lazy { GenericPrivateKeyFactory(Algorithms.KYBER, random) }
    override val publicKey: PublicKeyFactory<KEMPublicKey> by lazy { KEMPublicKeyFactory(Algorithms.KYBER, random) }
}