package org.sollecitom.chassis.cryptography.domain.asymmetric.signing

interface SigningAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY : SigningPrivateKey, PUBLIC_KEY : VerifyingPublicKey>