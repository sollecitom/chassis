package com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.algorithm.AsymmetricAlgorithm

interface SigningAlgorithm<KEY_GENERATION_ARGUMENTS> : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.algorithm.AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, SigningPrivateKey, VerifyingPublicKey>