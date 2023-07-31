package org.sollecitom.chassis.cryptography.implementation.bouncycastle

import org.sollecitom.chassis.cryptography.domain.key.KeyMetadata
import javax.crypto.spec.SecretKeySpec

internal data class JavaKeySpecMetadataAdapter(private val keySpec: SecretKeySpec) : KeyMetadata {

    override val algorithm: String get() = keySpec.algorithm
    override val format: String get() = keySpec.format

}