package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.signing

import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.SigningAlgorithm
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.AsymmetricAlgorithm

interface SigningAlgorithm<KEY_GENERATION_ARGUMENTS> : SigningAlgorithm<KEY_GENERATION_ARGUMENTS>, AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, SigningPrivateKey, VerifyingPublicKey>