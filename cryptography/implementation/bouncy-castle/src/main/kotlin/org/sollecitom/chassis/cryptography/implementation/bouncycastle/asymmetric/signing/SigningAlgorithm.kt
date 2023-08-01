package org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.signing

import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.SigningAlgorithm
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.SigningPrivateKey
import org.sollecitom.chassis.cryptography.domain.asymmetric.signing.VerifyingPublicKey
import org.sollecitom.chassis.cryptography.implementation.bouncycastle.asymmetric.AsymmetricAlgorithm

// TODO remove these key PUBLIC_KEY and PRIVATE_KEY generics
interface SigningAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY : SigningPrivateKey, PUBLIC_KEY : VerifyingPublicKey> : SigningAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY, PUBLIC_KEY>, AsymmetricAlgorithm<KEY_GENERATION_ARGUMENTS, PRIVATE_KEY, PUBLIC_KEY>