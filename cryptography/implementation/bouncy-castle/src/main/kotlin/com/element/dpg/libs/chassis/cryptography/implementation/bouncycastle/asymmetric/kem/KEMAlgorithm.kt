package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.kem

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMAlgorithm
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.AsymmetricAlgorithm

interface KEMAlgorithm<KEY_GENERATION_ARGUMENTS> : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMAlgorithm<KEY_GENERATION_ARGUMENTS>, AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey, com.element.dpg.libs.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey>