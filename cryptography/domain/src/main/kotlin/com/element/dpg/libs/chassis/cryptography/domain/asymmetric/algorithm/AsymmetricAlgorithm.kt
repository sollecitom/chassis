package com.element.dpg.libs.chassis.cryptography.domain.asymmetric.algorithm

import com.element.dpg.libs.chassis.cryptography.domain.algorithm.Algorithm
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PublicKey

interface AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PrivateKey, PUBLIC_KEY : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PublicKey> : com.element.dpg.libs.chassis.cryptography.domain.algorithm.Algorithm