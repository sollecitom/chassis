package org.sollecitom.chassis.cryptography.domain.asymmetric

interface AsymmetricKeyPair<PUBLIC : PublicKey> {

    val public: PUBLIC
    val private: PrivateKey
}