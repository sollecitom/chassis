package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric

import org.sollecitom.chassis.cryptography.domain.asymmetric.CrystalsAlgorithmSelector
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem.kyber.Kyber
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing.dilithium.Dilithium
import java.security.SecureRandom

internal class CrystalsAlgorithmCustomizer(private val random: SecureRandom) : CrystalsAlgorithmSelector {

    override val kyber by lazy { Kyber.keyPairGenerationOperations(random) }
    override val dilithium by lazy { Dilithium.keyPairGenerationOperations(random) }
}