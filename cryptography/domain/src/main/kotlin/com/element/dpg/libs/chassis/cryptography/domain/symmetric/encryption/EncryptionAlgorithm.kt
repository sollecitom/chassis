package com.element.dpg.libs.chassis.cryptography.domain.symmetric.encryption

import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKey
import org.sollecitom.chassis.cryptography.domain.symmetric.algorithm.SymmetricAlgorithm

interface EncryptionAlgorithm<KEY_GENERATION_ARGUMENTS> : SymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, SymmetricKey>