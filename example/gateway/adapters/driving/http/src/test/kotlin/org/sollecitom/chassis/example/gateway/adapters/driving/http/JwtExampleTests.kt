package org.sollecitom.chassis.example.gateway.adapters.driving.http

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.jose4j.jwa.AlgorithmConstraints
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers
import org.jose4j.jwe.JsonWebEncryption
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers
import org.jose4j.jwk.OctetKeyPairJsonWebKey
import org.jose4j.jwk.OkpJwkGenerator
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.consumer.InvalidJwtException
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logging.standard.configuration.configureLogging


@TestInstance(PER_CLASS)
private class JwtExampleTests : CoreDataGenerator by CoreDataGenerator.testProvider { // CFR https://bitbucket.org/b_c/jose4j/wiki/JWT%20Examples#markdown-header-producing-and-consuming-signed-and-encrypted-jwt-using-rfc8037s-ed25519-eddsa-and-x25519-ecdh

    init {
        configureLogging(defaultMinimumLoggingLevel = LoggingLevel.INFO)
    }

    @Test
    fun `encrypted JWT issuance and verification using RFC8037 Ed25519 EdDSA and X25519 ECDH`() {

        val issuerJwk = OkpJwkGenerator.generateJwk(OctetKeyPairJsonWebKey.SUBTYPE_ED25519).also { it.keyId = "issuer key" }
        val receiverJwk = OkpJwkGenerator.generateJwk(OctetKeyPairJsonWebKey.SUBTYPE_X25519).also { it.keyId = "receiver key" }
        val claims = JwtClaims()
        claims.issuer = "issuer" // who creates the token and signs it
        claims.setAudience("receiver") // to whom the token is intended to be sent
        claims.setExpirationTimeMinutesInTheFuture(10f) // time when the token will expire (10 minutes from now)
        claims.setGeneratedJwtId() // a unique identifier for the token
        claims.setIssuedAtToNow() // when the token was issued/created (now)
        claims.setNotBeforeMinutesInThePast(2f) // time before which the token is not yet valid (2 minutes ago)
        claims.subject = "subject" // the subject/principal is whom the token is about
        claims.setClaim("email", "mail@example.com") // additional claims/attributes about the subject can be added
        val groups: List<String> = mutableListOf("group-1", "other-group", "group-3")
        claims.setStringListClaim("groups", groups) // multivalued claims work too and will end up as a JSON array
        val address: MutableMap<String, String> = LinkedHashMap() // complex claim values can be set with a Map and will end up as a JSON Object
        address["street_address"] = "123 Main St"
        address["locality"] = "Anytown"
        address["region"] = "Anystate"
        address["country"] = "US"
        claims.setClaim("address", address)

        val jws = JsonWebSignature()
        jws.payload = claims.toJson()
        jws.key = issuerJwk.privateKey
        jws.keyIdHeaderValue = issuerJwk.keyId
        jws.algorithmHeaderValue = AlgorithmIdentifiers.EDDSA
        val innerJwt = jws.compactSerialization

        println("Inner JWT (before encryption): $innerJwt")


        // The outer JWT is a JWE
        val jwe = JsonWebEncryption()
        // The output of the X25519 ECDH-ES key agreement and KDF will be the content encryption key
        jwe.algorithmHeaderValue = KeyManagementAlgorithmIdentifiers.ECDH_ES
        // The content encryption key is used to encrypt the payload with a composite AES-CBC / HMAC SHA2 encryption algorithm
        val encAlg = ContentEncryptionAlgorithmIdentifiers.AES_256_CBC_HMAC_SHA_512
        jwe.encryptionMethodHeaderParameter = encAlg
        // We encrypt to the receiver using their public key
        jwe.key = receiverJwk.publicKey
        jwe.keyIdHeaderValue = receiverJwk.keyId
        // A nested JWT requires that the cty (Content Type) header be set to "JWT" in the outer JWT
        jwe.contentTypeHeaderValue = "JWT"
        // The inner JWT is the payload of the outer JWT
        jwe.payload = innerJwt
        // Produce the JWE compact serialization, which is the complete JWT/JWE representation, which is a string consisting of five dot ('.') separated base64url-encoded parts in the form Header.EncryptedKey.IV.Ciphertext.AuthenticationTag
        val jwt = jwe.compactSerialization
        // Now you can do something with the JWT. Like send it to some other party over the clouds and through the interwebs.
        println("JWT: $jwt")

        val jwsAlgConstraints = AlgorithmConstraints(ConstraintType.PERMIT, AlgorithmIdentifiers.EDDSA)
        val jweAlgConstraints = AlgorithmConstraints(ConstraintType.PERMIT, KeyManagementAlgorithmIdentifiers.ECDH_ES)
        val jweEncConstraints = AlgorithmConstraints(ConstraintType.PERMIT, ContentEncryptionAlgorithmIdentifiers.AES_256_CBC_HMAC_SHA_512)

        val jwtConsumer = JwtConsumerBuilder()
                .setRequireExpirationTime() // the JWT must have an expiration time
                .setMaxFutureValidityInMinutes(300) // but the  expiration time can't be too crazy
                .setRequireSubject() // the JWT must have a subject claim
                .setExpectedIssuer("issuer") // whom the JWT needs to have been issued by
                .setExpectedAudience("receiver") // to whom the JWT is intended for
                .setDecryptionKey(receiverJwk.privateKey) // decrypt with the receiver's private key
                .setVerificationKey(issuerJwk.publicKey) // verify the signature with the issuer's public key
                .setJwsAlgorithmConstraints(jwsAlgConstraints) // limits the acceptable signature algorithm(s)
                .setJweAlgorithmConstraints(jweAlgConstraints) // limits acceptable encryption key establishment algorithm(s)
                .setJweContentEncryptionAlgorithmConstraints(jweEncConstraints) // limits acceptable content encryption algorithm(s)
                .build() // create the JwtConsumer instance

        try {
            //  Validate the JWT and process it to the Claims
            val jwtClaims = jwtConsumer.processToClaims(jwt)
            println("JWT validation succeeded! " + jwtClaims.rawJson)
            assertThat(jwtClaims.toJson()).isEqualTo(claims.toJson()) // not sure if this is reliable enough
        } catch (e: InvalidJwtException) {
            // InvalidJwtException will be thrown, if the JWT failed processing or validation in any way.
            // Hopefully with meaningful explanations(s) about what went wrong.
            println("Invalid JWT! $e")
        }
    }
}