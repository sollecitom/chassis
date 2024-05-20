package com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle

import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider
import com.element.dpg.libs.chassis.cryptography.domain.asymmetric.AsymmetricAlgorithmFamilySelector
import com.element.dpg.libs.chassis.cryptography.domain.factory.CryptographicOperations
import com.element.dpg.libs.chassis.cryptography.domain.symmetric.SymmetricAlgorithmFamilySelector
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.asymmetric.AsymmetricAlgorithmFamilyCustomizer
import com.element.dpg.libs.chassis.cryptography.implementation.bouncycastle.symmetric.SymmetricAlgorithmFamilyCustomizer
import java.security.SecureRandom
import java.security.Security

private class BouncyCastleCryptographicOperations(private val random: SecureRandom) : CryptographicOperations {

    override val asymmetric: AsymmetricAlgorithmFamilySelector by lazy { AsymmetricAlgorithmFamilyCustomizer(random) }
    override val symmetric: SymmetricAlgorithmFamilySelector by lazy { SymmetricAlgorithmFamilyCustomizer(random) }

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