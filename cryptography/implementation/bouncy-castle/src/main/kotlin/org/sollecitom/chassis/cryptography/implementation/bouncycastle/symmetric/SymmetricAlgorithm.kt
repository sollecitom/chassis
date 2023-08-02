package org.sollecitom.chassis.cryptography.implementation.bouncycastle.symmetric

import org.sollecitom.chassis.cryptography.domain.symmetric.SecretKeyGenerationOperations
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKey
import java.security.SecureRandom

interface SymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, KEY : SymmetricKey> {

    fun secretKeyGenerationOperations(random: SecureRandom): SecretKeyGenerationOperations<KEY_GENERATION_ARGUMENTS, KEY>
}