package org.sollecitom.chassis.jwt.jose4j.examples

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.json.test.utils.containsSameEntriesAs
import org.sollecitom.chassis.jwt.domain.JwtContentEncryptionAlgorithm
import org.sollecitom.chassis.jwt.jose4j.utils.expiryTime
import org.sollecitom.chassis.jwt.jose4j.utils.issuingTime
import org.sollecitom.chassis.jwt.jose4j.utils.notBeforeTime
import org.sollecitom.chassis.jwt.test.utils.*
import org.sollecitom.chassis.kotlin.extensions.time.truncatedToSeconds
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logging.standard.configuration.configureLogging
import org.sollecitom.chassis.test.utils.assertions.containsSameElementsAs
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@TestInstance(PER_CLASS)
private class JwtExampleTests : CoreDataGenerator by CoreDataGenerator.testProvider { // CFR https://bitbucket.org/b_c/jose4j/wiki/JWT%20Examples#markdown-header-producing-and-consuming-signed-and-encrypted-jwt-using-rfc8037s-ed25519-eddsa-and-x25519-ecdh

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.INFO)
    }

    @Test
    fun `Ed25519 EdDSA issuance and verification`() { // TODO finish this and migrate the types

        val issuerKeyId = "issuer key"
        val issuer = newED25519JwtIssuer(issuerKeyId)
        val audienceName = "audience".let(::Name)
        val processor = newJwtProcessor(issuer.name, issuer.publicKey)
        val jwtId = newId.ulid.monotonic().stringValue
        val subject = "subject"
        val issuingTime = clock.now()
        val expiryTime = clock.now() + 30.minutes
        val notBeforeTime = issuingTime - 5.seconds
        val rolesClaim = "roles"
        val roles = mutableListOf("role-1", "role-2")
        val claimsJson = jwtClaimsJson {
            it.jwtId = jwtId
            it.issuer = issuer.name.value
            it.setAudience(audienceName.value)
            it.subject = subject
            it.issuingTime = issuingTime
            it.expiryTime = expiryTime
            it.notBeforeTime = notBeforeTime
            it.setStringListClaim(rolesClaim, roles)
        }

        val issuedJwt = issuer.issueJwt(claimsJson)
        val processedJwt = processor.readAndVerify(issuedJwt)

        assertThat(processedJwt.id).isEqualTo(jwtId)
        assertThat(processedJwt.subject).isEqualTo(subject)
        assertThat(processedJwt.issuerName).isEqualTo(issuer.name)
        assertThat(processedJwt.audienceNames).containsOnly(audienceName)
        assertThat(processedJwt.issuedAt).isEqualTo(issuingTime.truncatedToSeconds())
        assertThat(processedJwt.expirationTime).isEqualTo(expiryTime.truncatedToSeconds())
        assertThat(processedJwt.notBeforeTime).isEqualTo(notBeforeTime.truncatedToSeconds())
        assertThat(processedJwt.getStringListClaimValue(rolesClaim)).containsSameElementsAs(roles)
        assertThat(processedJwt.claimsAsJson).containsSameEntriesAs(claimsJson)
    }

    @Test
    fun `RFC8037 Ed25519 EdDSA and X25519 ECDH JWT issuance and verification`() {

        val issuerKeyId = "issuer key"
        val issuer = newED25519JwtIssuer(issuerKeyId)
        val audienceKeyId = "audience key"
        val audience = newX25519JwtAudience(audienceKeyId)
        val processor = newAudienceSpecificJwtProcessor(audience, issuer.name, issuer.publicKey, acceptableContentEncryptionAlgorithms = setOf(JwtContentEncryptionAlgorithm.AES_256_CBC_HMAC_SHA_512))
        val jwtId = newId.ulid.monotonic().stringValue
        val subject = "subject"
        val issuingTime = clock.now()
        val expiryTime = clock.now() + 30.minutes
        val notBeforeTime = issuingTime - 5.seconds
        val rolesClaim = "roles"
        val roles = mutableListOf("role-1", "role-2")
        val claimsJson = jwtClaimsJson {
            it.jwtId = jwtId
            it.issuer = issuer.name.value
            it.setAudience(audience.name.value)
            it.subject = subject
            it.issuingTime = issuingTime
            it.expiryTime = expiryTime
            it.notBeforeTime = notBeforeTime
            it.setStringListClaim(rolesClaim, roles)
        }

        val issuedEncryptedJwt = issuer.issueEncryptedJwt(claimsJson, audience, contentEncryptionAlgorithm = JwtContentEncryptionAlgorithm.AES_256_CBC_HMAC_SHA_512)
        val processedJwt = processor.readAndVerify(issuedEncryptedJwt)

        assertThat(processedJwt.id).isEqualTo(jwtId)
        assertThat(processedJwt.subject).isEqualTo(subject)
        assertThat(processedJwt.issuerName).isEqualTo(issuer.name)
        assertThat(processedJwt.audienceNames).containsOnly(audience.name)
        assertThat(processedJwt.issuedAt).isEqualTo(issuingTime.truncatedToSeconds())
        assertThat(processedJwt.expirationTime).isEqualTo(expiryTime.truncatedToSeconds())
        assertThat(processedJwt.notBeforeTime).isEqualTo(notBeforeTime.truncatedToSeconds())
        assertThat(processedJwt.getStringListClaimValue(rolesClaim)).containsSameElementsAs(roles)
        assertThat(processedJwt.claimsAsJson).containsSameEntriesAs(claimsJson)
    }
}