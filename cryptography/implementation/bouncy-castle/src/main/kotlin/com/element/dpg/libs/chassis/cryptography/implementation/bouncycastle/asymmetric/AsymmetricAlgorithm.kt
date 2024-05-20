package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.KeyPairGenerationOperations
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PublicKey
import java.security.SecureRandom

interface AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY : PrivateKey, PUBLIC_KEY : PublicKey> {

    fun keyPairGenerationOperations(random: SecureRandom): KeyPairGenerationOperations<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY, PUBLIC_KEY>
}