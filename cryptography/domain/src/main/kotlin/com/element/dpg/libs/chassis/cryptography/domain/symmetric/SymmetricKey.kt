package com.element.dpg.libs.chassis.cryptography.domain.symmetric

import com.element.dpg.libs.chassis.cryptography.domain.key.CryptographicKey

interface SymmetricKey : CryptographicKey {

    val ctr: EncryptionMode.CTR.Operations
}