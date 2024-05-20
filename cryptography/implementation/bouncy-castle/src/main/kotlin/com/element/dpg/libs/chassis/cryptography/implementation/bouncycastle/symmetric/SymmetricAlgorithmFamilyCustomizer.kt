package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.symmetric

import com.element.dpg.libs.chassis.cryptography.domain.symmetric.SecretKeyGenerationOperations
import com.element.dpg.libs.chassis.cryptography.domain.symmetric.SymmetricAlgorithmFamilySelector
import com.element.dpg.libs.chassis.cryptography.domain.symmetric.SymmetricKey
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.symmetric.encryption.aes.AES
import java.security.SecureRandom
import com.element.dpg.libs.chassis.cryptography.domain.symmetric.encryption.aes.AES as AESAlgorithm

internal class SymmetricAlgorithmFamilyCustomizer(private val random: SecureRandom) : SymmetricAlgorithmFamilySelector {

    override val aes: SecretKeyGenerationOperations<AESAlgorithm.KeyArguments, SymmetricKey> by lazy { AES.secretKeyGenerationOperations(random) }
}