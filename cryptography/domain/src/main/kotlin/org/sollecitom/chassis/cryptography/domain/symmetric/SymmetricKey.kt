package org.sollecitom.chassis.cryptography.domain.symmetric

import org.sollecitom.chassis.cryptography.domain.key.CryptographicKey

interface SymmetricKey : CryptographicKey {

    val ctr: EncryptionMode.CTR.Operations
}