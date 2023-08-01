package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric

import org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.PublicKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithms.KeyPairGenerationOperations
import java.security.SecureRandom

interface AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY : PrivateKey, PUBLIC_KEY : PublicKey> {

    fun keyPairGenerationOperations(random: SecureRandom): KeyPairGenerationOperations<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY, PUBLIC_KEY>
}