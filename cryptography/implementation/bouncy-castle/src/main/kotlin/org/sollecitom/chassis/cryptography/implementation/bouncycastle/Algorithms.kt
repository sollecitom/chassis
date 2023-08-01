package org.sollecitom.chassis.cryptography.implementation.bouncycastle

import org.bouncycastle.pqc.jcajce.spec.DilithiumParameterSpec

internal object Algorithms { // TODO break into separate types (1 per algo) and delete

    object Kyber {
        const val NAME = "KYBER"
    }

    object Dilithium {

        const val NAME = "Dilithium"

        enum class Variant(val signatureAlgorithm: String, val algorithmParameterSpec: DilithiumParameterSpec) {

            DILITHIUM_2("DILITHIUM2", DilithiumParameterSpec.dilithium2),
            DILITHIUM_3("DILITHIUM3", DilithiumParameterSpec.dilithium3),
            DILITHIUM_5("DILITHIUM5", DilithiumParameterSpec.dilithium5),
            DILITHIUM_2_AES("DILITHIUM2-AES", DilithiumParameterSpec.dilithium2_aes),
            DILITHIUM_3_AES("DILITHIUM3-AES", DilithiumParameterSpec.dilithium3_aes),
            DILITHIUM_5_AES("DILITHIUM5-AES", DilithiumParameterSpec.dilithium5_aes),
        }
    }
}