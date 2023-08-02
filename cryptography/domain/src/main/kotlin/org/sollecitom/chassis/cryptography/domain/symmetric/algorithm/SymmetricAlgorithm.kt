package org.sollecitom.chassis.cryptography.domain.symmetric.algorithm

import org.sollecitom.chassis.cryptography.domain.algorithm.Algorithm
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKey

interface SymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, KEY : SymmetricKey> : Algorithm