package org.sollecitom.chassis.cryptography.domain.asymmetric.kem

import org.sollecitom.chassis.cryptography.domain.algorithm.Algorithm

interface KEMAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY : KEMPrivateKey, PUBLIC_KEY : KEMPublicKey> : Algorithm