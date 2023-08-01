package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.kyber

import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.kyber.Kyber
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMPrivateKeyFactory
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.KEMPublicKeyFactory
import java.security.SecureRandom

// TODO merge this with Kyber?
internal class KyberAlgorithmOperationCustomizer(private val random: SecureRandom) : Kyber.Operations {

    override val keyPair by lazy { KyberKeyPairFactory(random) }
    override val privateKey by lazy { KEMPrivateKeyFactory(Kyber.NAME, random) }
    override val publicKey by lazy { KEMPublicKeyFactory(Kyber.NAME, random) }
}