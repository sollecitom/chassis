package org.sollecitom.chassis.cryptography.domain.asymmetric.algorithm

import org.sollecitom.chassis.cryptography.domain.algorithm.Algorithm
import org.sollecitom.chassis.cryptography.domain.asymmetric.PrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.PublicKey

interface AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY : PrivateKey, PUBLIC_KEY : PublicKey> : Algorithm