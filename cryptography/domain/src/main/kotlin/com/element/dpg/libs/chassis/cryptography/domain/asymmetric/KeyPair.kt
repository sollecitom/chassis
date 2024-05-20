package com.element.dpg.libs.chassis.cryptography.domain.asymmetric

data class KeyPair<out PRIVATE : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PrivateKey, out PUBLIC : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PublicKey>(override val private: PRIVATE, override val public: PUBLIC) : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.AsymmetricKeyPair<PRIVATE, PUBLIC> {

    init {
        require(public.algorithm == private.algorithm) { "Public and private key must have the same algorithm" }
    }
}