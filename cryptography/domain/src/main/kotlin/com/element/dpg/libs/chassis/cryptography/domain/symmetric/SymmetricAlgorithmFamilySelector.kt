package com.element.dpg.libs.chassis.cryptography.domain.symmetric

import com.element.dpg.libs.chassis.cryptography.domain.symmetric.encryption.aes.AES

interface SymmetricAlgorithmFamilySelector {

    val aes: SecretKeyGenerationOperations<AES.KeyArguments, SymmetricKey>
}