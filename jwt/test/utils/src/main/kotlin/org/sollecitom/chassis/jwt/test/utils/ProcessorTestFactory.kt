package org.sollecitom.chassis.jwt.test.utils

import org.jose4j.jwa.AlgorithmConstraints
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers
import org.jose4j.jwk.HttpsJwks
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.jwt.domain.JwtAudience
import org.sollecitom.chassis.jwt.domain.JwtContentEncryptionAlgorithm
import org.sollecitom.chassis.jwt.domain.JwtProcessor
import org.sollecitom.chassis.jwt.jose4j.processor.JoseJwtProcessor
import java.net.URI
import java.security.PublicKey

fun newJwtProcessor(issuerName: Name, issuerPublicKey: PublicKey, configuration: JwtProcessor.Configuration = newJwtProcessorConfiguration()): JwtProcessor {

    val builder = JwtConsumerBuilder()
    builder.setExpectedIssuer(issuerName.value)
    builder.setSkipDefaultAudienceValidation()
    builder.setVerificationKey(issuerPublicKey)
    if (configuration.requireSubject) {
        builder.setRequireSubject()
    }
    if (configuration.requireIssuedAt) {
        builder.setRequireIssuedAt()
    }
    if (configuration.requireExpirationTime) {
        builder.setRequireExpirationTime()
    }
    configuration.maximumFutureValidityInMinutes?.let(builder::setMaxFutureValidityInMinutes)
    builder.setJwsAlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, *configuration.acceptableSignatureAlgorithms.toTypedArray())
    return JoseJwtProcessor(builder.build())
}

fun newJKSJwtProcessor(issuerName: Name, jksUrl: URI): JwtProcessor {

    val builder = JwtConsumerBuilder()
    builder.setExpectedIssuer(issuerName.value)
    builder.setSkipDefaultAudienceValidation()
    builder.setVerificationKeyResolver(HttpsJwksVerificationKeyResolver(HttpsJwks(jksUrl.toString())))
    return JoseJwtProcessor(builder.build())
}

fun newAudienceSpecificJwtProcessor(audience: JwtAudience, issuerName: Name, issuerPublicKey: PublicKey, configuration: JwtProcessor.Configuration = newJwtProcessorConfiguration()): JwtProcessor {

    val builder = JwtConsumerBuilder()
    builder.setExpectedIssuer(issuerName.value)
    builder.setVerificationKey(issuerPublicKey)
    builder.setExpectedAudience(audience.name.value)
    builder.setDecryptionKey(audience.privateKey)
    if (configuration.requireSubject) {
        builder.setRequireSubject()
    }
    if (configuration.requireIssuedAt) {
        builder.setRequireIssuedAt()
    }
    if (configuration.requireExpirationTime) {
        builder.setRequireExpirationTime()
    }
    configuration.maximumFutureValidityInMinutes?.let(builder::setMaxFutureValidityInMinutes)
    builder.setJwsAlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, *configuration.acceptableSignatureAlgorithms.toTypedArray())
    builder.setJweAlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, *configuration.acceptableEncryptionKeyEstablishmentAlgorithms.toTypedArray())
    builder.setJweContentEncryptionAlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, *configuration.acceptableContentEncryptionAlgorithms.map { it.value }.toTypedArray())
    return JoseJwtProcessor(builder.build())
}

fun newAudienceSpecificJwtProcessor(audience: JwtAudience, issuerName: Name, issuerPublicKey: PublicKey, acceptableSignatureAlgorithms: Set<String> = setOf(AlgorithmIdentifiers.EDDSA), acceptableEncryptionKeyEstablishmentAlgorithms: Set<String> = setOf(KeyManagementAlgorithmIdentifiers.ECDH_ES), acceptableContentEncryptionAlgorithms: Set<JwtContentEncryptionAlgorithm> = setOf(JwtContentEncryptionAlgorithm.AES_256_CBC_HMAC_SHA_512)): JwtProcessor = newAudienceSpecificJwtProcessor(audience, issuerName, issuerPublicKey, newJwtProcessorConfiguration(acceptableSignatureAlgorithms = acceptableSignatureAlgorithms, acceptableEncryptionKeyEstablishmentAlgorithms = acceptableEncryptionKeyEstablishmentAlgorithms, acceptableContentEncryptionAlgorithms = acceptableContentEncryptionAlgorithms))