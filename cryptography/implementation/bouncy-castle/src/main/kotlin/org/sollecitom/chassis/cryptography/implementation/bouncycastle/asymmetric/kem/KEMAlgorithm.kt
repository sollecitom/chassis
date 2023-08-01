package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem

import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMAlgorithm
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.AsymmetricAlgorithm

interface KEMAlgorithm<KEY_GENERATION_ARGUMENTS> : KEMAlgorithm<KEY_GENERATION_ARGUMENTS>, AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, KEMPrivateKey, KEMPublicKey>