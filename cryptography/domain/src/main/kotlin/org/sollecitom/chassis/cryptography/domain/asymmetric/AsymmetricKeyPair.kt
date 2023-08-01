package org.sollecitom.chassis.cryptography.domain.asymmetric

interface AsymmetricKeyPair<out PRIVATE : PrivateKey, out PUBLIC : PublicKey> {

    val private: PRIVATE
    val public: PUBLIC
}