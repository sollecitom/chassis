package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.symmetric

import com.element.dpg.libs.chassis.cryptography.domain.symmetric.SecretKeyGenerationOperations
import com.element.dpg.libs.chassis.cryptography.domain.symmetric.SymmetricKey
import java.security.SecureRandom

interface SymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, KEY : SymmetricKey> {

    fun secretKeyGenerationOperations(random: SecureRandom): SecretKeyGenerationOperations<KEY_GENERATION_ARGUMENTS, KEY>
}