package org.sollecitom.chassis.cryptography.domain.asymmetric.signing

import org.sollecitom.chassis.cryptography.domain.algorithm.Algorithm

interface SigningAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY : SigningPrivateKey, PUBLIC_KEY : VerifyingPublicKey> : Algorithm