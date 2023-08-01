package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.kyber

import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.KyberAlgorithmOperationSelector
import org.sollecitom.chassis.cryptography.domain.algorithms.kyber.KyberKeyPairArguments
import org.sollecitom.chassis.cryptography.domain.asymmetric.KEMPublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.KeyPairFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PrivateKeyFactory
import org.sollecitom.chassis.cryptography.domain.asymmetric.factory.PublicKeyFactory
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.Algorithms
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.GenericPrivateKeyFactory
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMPublicKeyFactory
import java.security.SecureRandom

internal class KyberAlgorithmOperationCustomizer(private val random: SecureRandom) : KyberAlgorithmOperationSelector {

    override val keyPair: KeyPairFactory<KyberKeyPairArguments, KEMPublicKey> by lazy { KyberKeyPairFactory(random) }
    override val privateKey: PrivateKeyFactory by lazy { GenericPrivateKeyFactory(Algorithms.Kyber.NAME, random) }
    override val publicKey: PublicKeyFactory<KEMPublicKey> by lazy { KEMPublicKeyFactory(Algorithms.Kyber.NAME, random) }
}