package org.sollecitom.chassis.cryptography.implementation.bouncycastle.symmetric

import org.sollecitom.chassis.cryptography.domain.symmetric.SecretKeyGenerationOperations
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricAlgorithmFamilySelector
import org.sollecitom.chassis.cryptography.domain.symmetric.SymmetricKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.symmetric.encryption.aes.AES
import java.security.SecureRandom
import org.sollecitom.chassis.cryptography.domain.symmetric.encryption.aes.AES as AESAlgorithm

internal class SymmetricAlgorithmFamilyCustomizer(private val random: SecureRandom) : SymmetricAlgorithmFamilySelector {

    override val aes: SecretKeyGenerationOperations<AESAlgorithm.KeyArguments, SymmetricKey> by lazy { AES.secretKeyGenerationOperations(random) }
}