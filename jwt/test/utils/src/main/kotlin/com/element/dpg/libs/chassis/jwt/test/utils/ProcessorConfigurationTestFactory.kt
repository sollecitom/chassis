package com.element.dpg.libs.chassis.jwt.test.utils

import com.element.dpg.libs.chassis.jwt.domain.JwtContentEncryptionAlgorithm
import com.element.dpg.libs.chassis.jwt.domain.JwtProcessor
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers
import org.jose4j.jws.AlgorithmIdentifiers

fun newJwtProcessorConfiguration(
        requireSubject: Boolean = true,
        requireIssuedAt: Boolean = true,
        requireExpirationTime: Boolean = true,
        maximumFutureValidityInMinutes: Int? = null,
        acceptableSignatureAlgorithms: Set<String> = setOf(AlgorithmIdentifiers.EDDSA),
        acceptableEncryptionKeyEstablishmentAlgorithms: Set<String> = setOf(KeyManagementAlgorithmIdentifiers.ECDH_ES),
        acceptableContentEncryptionAlgorithms: Set<JwtContentEncryptionAlgorithm> = setOf(JwtContentEncryptionAlgorithm.AES_256_CBC_HMAC_SHA_512)
) = JwtProcessor.Configuration(requireSubject, requireIssuedAt, requireExpirationTime, maximumFutureValidityInMinutes, acceptableSignatureAlgorithms, acceptableEncryptionKeyEstablishmentAlgorithms, acceptableContentEncryptionAlgorithms)