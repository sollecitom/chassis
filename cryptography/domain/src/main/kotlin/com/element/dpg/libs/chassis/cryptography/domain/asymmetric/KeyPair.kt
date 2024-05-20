package com.element.dpg.libs.chassis.cryptography.domain.asymmetric

data class KeyPair<out PRIVATE : PrivateKey, out PUBLIC : PublicKey>(override val private: PRIVATE, override val public: PUBLIC) : AsymmetricKeyPair<PRIVATE, PUBLIC> {

    init {
        require(public.algorithm == private.algorithm) { "Public and private key must have the same algorithm" }
    }
}