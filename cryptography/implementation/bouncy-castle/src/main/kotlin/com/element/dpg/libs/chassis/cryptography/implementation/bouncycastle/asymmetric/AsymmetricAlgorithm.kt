package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.KeyPairGenerationOperations
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PublicKey
import java.security.SecureRandom

interface AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PrivateKey, PUBLIC_KEY : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PublicKey> {

    fun keyPairGenerationOperations(random: SecureRandom): com.element.dpg.libs.chassis.cryptography.domain.asymmetric.KeyPairGenerationOperations<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY, PUBLIC_KEY>
}