package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.signing

import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.SigningAlgorithm
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.AsymmetricAlgorithm

interface SigningAlgorithm<KEY_GENERATION_ARGUMENTS> : SigningAlgorithm<KEY_GENERATION_ARGUMENTS>, AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, SigningPrivateKey, VerifyingPublicKey>