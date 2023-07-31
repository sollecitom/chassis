package org.sollecitom.chassis.cryptography.domain.key

interface CryptographicKey {

    val encoded: ByteArray
    val metadata: KeyMetadata
}