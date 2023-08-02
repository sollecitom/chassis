package org.sollecitom.chassis.cryptography.domain.symmetric

import org.sollecitom.chassis.cryptography.domain.symmetric.encryption.aes.AES

interface SymmetricAlgorithmFamilySelector {

    val aes: SecretKeyGenerationOperations<AES.KeyArguments, SymmetricKey>
}