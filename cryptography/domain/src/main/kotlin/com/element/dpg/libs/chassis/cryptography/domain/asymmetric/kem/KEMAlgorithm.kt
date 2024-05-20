package com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.algorithm.AsymmetricAlgorithm

interface KEMAlgorithm<KEY_GENERATION_ARGUMENTS> : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.algorithm.AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey>