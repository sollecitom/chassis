package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.kem

import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMAlgorithm
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.kem.KEMPublicKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.AsymmetricAlgorithm

// TODO remove these key PUBLIC_KEY and PRIVATE_KEY generics
interface KEMAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY : KEMPrivateKey, PUBLIC_KEY : KEMPublicKey> : KEMAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY, PUBLIC_KEY>, AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY, PUBLIC_KEY>