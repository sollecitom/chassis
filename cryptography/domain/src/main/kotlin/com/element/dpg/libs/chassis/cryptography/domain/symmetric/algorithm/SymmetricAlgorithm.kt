package com.element.dpg.libs.chassis.cryptography.domain.symmetric.algorithm

import com.element.dpg.libs.chassis.cryptography.domain.symmetric.SymmetricKey

interface SymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, KEY : SymmetricKey> : com.element.dpg.libs.chassis.cryptography.domain.algorithm.Algorithm