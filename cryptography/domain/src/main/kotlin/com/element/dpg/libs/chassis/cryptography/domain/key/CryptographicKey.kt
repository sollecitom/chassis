package com.element.dpg.libs.chassis.cryptography.domain.key

interface CryptographicKey {

    val encoded: ByteArray
    val encodedAsHexString: String
    val algorithm: String
    val format: String

    /**
     * Deterministic hash code for the key (i.e. not depending on JVM version, runtime, etc.), could be used for lookups in external systems
     */
    val hash: Long
}