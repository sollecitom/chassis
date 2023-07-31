package org.sollecitom.chassis.cryptography.domain.asymmetric

interface AsymmetricKeyPair {

    val public: PublicKey
    val private: PrivateKey
}