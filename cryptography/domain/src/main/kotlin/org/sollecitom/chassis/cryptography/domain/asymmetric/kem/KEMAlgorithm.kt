package org.sollecitom.chassis.cryptography.domain.asymmetric.kem

import org.sollecitom.chassis.cryptography.domain.asymmetric.algorithm.AsymmetricAlgorithm

interface KEMAlgorithm<KEY_GENERATION_ARGUMENTS> : AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, KEMPrivateKey, KEMPublicKey>