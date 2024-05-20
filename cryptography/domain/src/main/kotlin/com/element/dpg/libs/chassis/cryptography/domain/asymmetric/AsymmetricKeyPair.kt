package com.element.dpg.libs.chassis.cryptography.domain.asymmetric

interface AsymmetricKeyPair<out PRIVATE : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PrivateKey, out PUBLIC : com.element.dpg.libs.chassis.cryptography.domain.asymmetric.PublicKey> {

    val private: PRIVATE
    val public: PUBLIC
}