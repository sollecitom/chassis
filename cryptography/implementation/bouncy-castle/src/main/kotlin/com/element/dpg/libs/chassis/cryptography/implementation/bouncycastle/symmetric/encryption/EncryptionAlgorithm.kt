package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.symmetric.encryption

import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKey
import org.sollecitom.chassis.cryptography.domain.symmetric.encryption.EncryptionAlgorithm
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.symmetric.SymmetricAlgorithm

interface EncryptionAlgorithm<KEY_GENERATION_ARGUMENTS> : EncryptionAlgorithm<KEY_GENERATION_ARGUMENTS>, SymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, SymmetricKey>