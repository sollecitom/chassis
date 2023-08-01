package org.sollecitom.chassis.cryptography.domain.asymmetric.signing

import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithm.AsymmetricAlgorithm

interface SigningAlgorithm<KEY_GENERATION_ARGUMENTS> : AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, SigningPrivateKey, VerifyingPublicKey>