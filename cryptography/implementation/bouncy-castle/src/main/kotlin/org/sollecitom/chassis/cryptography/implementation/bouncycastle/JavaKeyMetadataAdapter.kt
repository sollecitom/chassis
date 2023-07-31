package org.sollecitom.chassis.cryptography.implementation.bouncycastle

import org.sollecitom.chassis.cryptography.domain.key.KeyMetadata
import java.security.Key

internal data class JavaKeyMetadataAdapter(private val key: Key) : KeyMetadata {

    override val algorithm: String get() = key.algorithm
    override val format: String get() = key.format
}