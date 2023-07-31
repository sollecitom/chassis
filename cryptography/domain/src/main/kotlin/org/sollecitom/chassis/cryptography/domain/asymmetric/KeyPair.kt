package org.sollecitom.chassis.cryptography.domain.asymmetric

data class KeyPair<PUBLIC : PublicKey>(override val public: PUBLIC, override val private: PrivateKey) : AsymmetricKeyPair<PUBLIC> {

    init {
        require(public.metadata.algorithm == private.metadata.algorithm) { "Public and private key must have the same algorithm" }
    }
}