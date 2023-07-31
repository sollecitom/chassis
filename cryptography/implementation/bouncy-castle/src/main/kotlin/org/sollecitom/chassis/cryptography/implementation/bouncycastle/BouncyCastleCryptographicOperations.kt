package org.sollecitom.chassis.cryptography.implementation.bouncycastle

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider
import org.sollecitom.chassis.cryptography.domain.factory.AsymmetricAlgorithmFamilySelector
import org.sollecitom.chassis.cryptography.domain.factory.CryptographicOperations
import java.security.SecureRandom
import java.security.Security

private class BouncyCastleCryptographicOperations(private val random: SecureRandom) : CryptographicOperations {

    override val asymmetric: AsymmetricAlgorithmFamilySelector by lazy { AsymmetricAlgorithmFamilyCustomizer(random) }

    companion object {
        init {
            if (Security.getProvider(BCPQC_PROVIDER) == null) {
                Security.addProvider(BouncyCastlePQCProvider())
            }
            if (Security.getProvider(BC_PROVIDER) == null) {
                Security.addProvider(BouncyCastleProvider())
            }
        }
    }
}

fun CryptographicOperations.Companion.bouncyCastle(random: SecureRandom = SecureRandom()): CryptographicOperations = BouncyCastleCryptographicOperations(random)