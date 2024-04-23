package org.sollecitom.chassis.jwt.jose4j.examples

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEqualTo
import kotlinx.datetime.Instant
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType
import org.jose4j.jwe.JsonWebEncryption
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers
import org.jose4j.jwk.HttpsJwks
import org.jose4j.jwk.OctetKeyPairJsonWebKey
import org.jose4j.jwk.OkpJwkGenerator
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import org.jose4j.jwt.consumer.JwtConsumer
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import org.jose4j.keys.resolvers.HttpsJwksVerificationKeyResolver
import org.json.JSONObject
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.core.domain.naming.Name
import org.sollecitom.chassis.core.test.utils.random
import org.sollecitom.chassis.core.test.utils.testProvider
import org.sollecitom.chassis.core.utils.CoreDataGenerator
import org.sollecitom.chassis.core.utils.RandomGenerator
import org.sollecitom.chassis.json.test.utils.containsSameEntriesAs
import org.sollecitom.chassis.kotlin.extensions.text.string
import org.sollecitom.chassis.kotlin.extensions.time.truncatedToSeconds
import org.sollecitom.chassis.logger.core.LoggingLevel
import org.sollecitom.chassis.logging.standard.configuration.configureLogging
import org.sollecitom.chassis.test.utils.assertions.containsSameElementsAs
import java.net.URI
import java.security.KeyPair
import java.security.PrivateKey
import java.security.PublicKey
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
        val claims = claims {
            it.jwtId = jwtId
            it.issuer = issuer.name.value
            it.setAudience(audienceName.value)
            it.subject = subject
            it.issuingTime = issuingTime
            it.expiryTime = expiryTime
            it.notBeforeTime = notBeforeTime
            it.setStringListClaim(rolesClaim, roles)
        }
        val claimsJson = claims.toJson().let(::JSONObject)

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
        val claims = claims {
            it.jwtId = jwtId
            it.issuer = issuer.name.value
            it.setAudience(audience.name.value)
            it.subject = subject
            it.issuingTime = issuingTime
            it.expiryTime = expiryTime
            it.notBeforeTime = notBeforeTime
            it.setStringListClaim(rolesClaim, roles)
        }
        val claimsJson = claims.toJson().let(::JSONObject)

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

    private fun claims(configure: (JwtClaims) -> Unit) = JwtClaims().also(configure)
}

interface JwtIssuer : JwtParty {

    fun issueEncryptedJwt(claims: JSONObject, audience: JwtAudience, contentEncryptionAlgorithm: JwtContentEncryptionAlgorithm = JwtContentEncryptionAlgorithm.AES_256_CBC_HMAC_SHA_512): String {

        val innerJwt = issueJwt(claims)
        return encryptJwt(innerJwt, audience, contentEncryptionAlgorithm)
    }

    fun issueJwt(claims: JSONObject): String

    fun encryptJwt(innerJwt: String, audience: JwtAudience, contentEncryptionAlgorithm: JwtContentEncryptionAlgorithm): String
}

interface JwtAudience : JwtParty {

    val keyId: String
    val privateKey: PrivateKey
}

interface JwtParty {

    val name: Name
    val publicKey: PublicKey
}

// TODO move to cryptography/test-utils and replace with bouncy-castle
context(RandomGenerator)
fun newKeyPair(keyType: String): KeyPair {

    val jwk = OkpJwkGenerator.generateJwk(keyType, null, secureRandom) // TODO pass a SecureRandom here (add that to RandomGenerator)
    return KeyPair(jwk.publicKey, jwk.privateKey)
}

context(RandomGenerator)
fun newED25519JwtIssuer(keyId: String = random.string(wordLength = 6), name: Name = Name.random()): JwtIssuer {

    val keyPair = newKeyPair(OctetKeyPairJsonWebKey.SUBTYPE_ED25519)
    return ED25519JoseIssuerAdapter(keyPair, keyId, name)
}

private class ED25519JoseIssuerAdapter(private val keyPair: KeyPair, private val keyId: String, override val name: Name) : JwtIssuer {

    init {
        require(keyPair.public.algorithm == EDDSA_ALGORITHM) { "Public key must use ${OctetKeyPairJsonWebKey.SUBTYPE_ED25519}" }
        require(keyPair.private.algorithm == EDDSA_ALGORITHM) { "Private key must use ${OctetKeyPairJsonWebKey.SUBTYPE_ED25519}" }
    }

    override val publicKey: PublicKey get() = keyPair.public

    override fun issueJwt(claims: JSONObject): String {

        val jws = JsonWebSignature()
        jws.payload = claims.toString()
        jws.key = keyPair.private
        jws.keyIdHeaderValue = keyId
        jws.algorithmHeaderValue = EDDSA_ALGORITHM
        return jws.compactSerialization
    }

    override fun encryptJwt(innerJwt: String, audience: JwtAudience, contentEncryptionAlgorithm: JwtContentEncryptionAlgorithm): String {

        val jwe = JsonWebEncryption()
        jwe.algorithmHeaderValue = KeyManagementAlgorithmIdentifiers.ECDH_ES
        jwe.encryptionMethodHeaderParameter = contentEncryptionAlgorithm.value
        jwe.key = audience.publicKey
        jwe.keyIdHeaderValue = audience.keyId
        jwe.contentTypeHeaderValue = OUTER_JWT_CONTENT_TYPE_HEADER_VALUE
        jwe.payload = innerJwt
        return jwe.compactSerialization
    }

    companion object {
        private const val OUTER_JWT_CONTENT_TYPE_HEADER_VALUE = "JWT"
        private const val EDDSA_ALGORITHM = AlgorithmIdentifiers.EDDSA
    }
}

context(RandomGenerator)
fun newX25519JwtAudience(keyId: String = random.string(wordLength = 6), name: Name = Name.random()): JwtAudience {

    val keyPair = newKeyPair(OctetKeyPairJsonWebKey.SUBTYPE_X25519)
    return X25519JoseAudienceAdapter(keyPair, keyId, name)
}

private class X25519JoseAudienceAdapter(private val keyPair: KeyPair, override val keyId: String, override val name: Name) : JwtAudience {

    init {
        require(keyPair.public.algorithm == KEY_ALGORITHM) { "Public key must use ${OctetKeyPairJsonWebKey.SUBTYPE_X25519}" }
        require(keyPair.private.algorithm == KEY_ALGORITHM) { "Private key must use ${OctetKeyPairJsonWebKey.SUBTYPE_X25519}" }
    }

    override val publicKey: PublicKey get() = keyPair.public
    override val privateKey: PrivateKey get() = keyPair.private

    companion object {
        private const val KEY_ALGORITHM = "XDH"
    }
}

enum class JwtContentEncryptionAlgorithm(val value: String) {
    AES_128_CBC_HMAC_SHA_256("A128CBC-HS256"), AES_192_CBC_HMAC_SHA_384("A192CBC-HS384"), AES_256_CBC_HMAC_SHA_512("A256CBC-HS512"), AES_128_GCM("A128GCM"), AES_192_GCM("A192GCM"), AES_256_GCM("A256GCM")
}

interface JwtProcessor {

    fun readAndVerify(jwt: String): JWT

    fun readWithoutVerifying(jwt: String): JWT

    data class Configuration(
            val requireSubject: Boolean,
            val requireIssuedAt: Boolean,
            val requireExpirationTime: Boolean,
            val maximumFutureValidityInMinutes: Int?,
            val acceptableSignatureAlgorithms: Set<String>,
            val acceptableEncryptionKeyEstablishmentAlgorithms: Set<String>,
            val acceptableContentEncryptionAlgorithms: Set<JwtContentEncryptionAlgorithm>
    )

    companion object
}

fun newJwtProcessorConfiguration(
        requireSubject: Boolean = true,
        requireIssuedAt: Boolean = true,
        requireExpirationTime: Boolean = true,
        maximumFutureValidityInMinutes: Int? = null,
        acceptableSignatureAlgorithms: Set<String> = setOf(AlgorithmIdentifiers.EDDSA),
        acceptableEncryptionKeyEstablishmentAlgorithms: Set<String> = setOf(KeyManagementAlgorithmIdentifiers.ECDH_ES),
        acceptableContentEncryptionAlgorithms: Set<JwtContentEncryptionAlgorithm> = setOf(JwtContentEncryptionAlgorithm.AES_256_CBC_HMAC_SHA_512)
) = JwtProcessor.Configuration(requireSubject, requireIssuedAt, requireExpirationTime, maximumFutureValidityInMinutes, acceptableSignatureAlgorithms, acceptableEncryptionKeyEstablishmentAlgorithms, acceptableContentEncryptionAlgorithms)

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
    if (configuration.maximumFutureValidityInMinutes != null) {
        builder.setMaxFutureValidityInMinutes(configuration.maximumFutureValidityInMinutes)
    }
    builder.setJwsAlgorithmConstraints(ConstraintType.PERMIT, *configuration.acceptableSignatureAlgorithms.toTypedArray())
    builder.setJweAlgorithmConstraints(ConstraintType.PERMIT, *configuration.acceptableEncryptionKeyEstablishmentAlgorithms.toTypedArray())
    builder.setJweContentEncryptionAlgorithmConstraints(ConstraintType.PERMIT, *configuration.acceptableContentEncryptionAlgorithms.map { it.value }.toTypedArray())
    return JoseJwtProcessor(builder.build())
}

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
    if (configuration.maximumFutureValidityInMinutes != null) {
        builder.setMaxFutureValidityInMinutes(configuration.maximumFutureValidityInMinutes)
    }
    builder.setJwsAlgorithmConstraints(ConstraintType.PERMIT, *configuration.acceptableSignatureAlgorithms.toTypedArray())
    return JoseJwtProcessor(builder.build())
}

fun newJKSJwtProcessor(issuerName: Name, jksUrl: URI): JwtProcessor {

    val builder = JwtConsumerBuilder()
    builder.setExpectedIssuer(issuerName.value)
    builder.setSkipDefaultAudienceValidation()
    builder.setVerificationKeyResolver(HttpsJwksVerificationKeyResolver(HttpsJwks(jksUrl.toString())))
    return JoseJwtProcessor(builder.build())
}

fun newAudienceSpecificJwtProcessor(audience: JwtAudience, issuerName: Name, issuerPublicKey: PublicKey, acceptableSignatureAlgorithms: Set<String> = setOf(AlgorithmIdentifiers.EDDSA), acceptableEncryptionKeyEstablishmentAlgorithms: Set<String> = setOf(KeyManagementAlgorithmIdentifiers.ECDH_ES), acceptableContentEncryptionAlgorithms: Set<JwtContentEncryptionAlgorithm> = setOf(JwtContentEncryptionAlgorithm.AES_256_CBC_HMAC_SHA_512)): JwtProcessor = newAudienceSpecificJwtProcessor(audience, issuerName, issuerPublicKey, newJwtProcessorConfiguration(acceptableSignatureAlgorithms = acceptableSignatureAlgorithms, acceptableEncryptionKeyEstablishmentAlgorithms = acceptableEncryptionKeyEstablishmentAlgorithms, acceptableContentEncryptionAlgorithms = acceptableContentEncryptionAlgorithms))

private class JoseJwtProcessor(private val consumer: JwtConsumer) : JwtProcessor {

    private val noChecksConsumer = JwtConsumerBuilder().setSkipAllValidators().setDisableRequireSignature().setSkipSignatureVerification().build()

    override fun readAndVerify(jwt: String) = consumer.processToClaims(jwt).toJwt()

    override fun readWithoutVerifying(jwt: String) = noChecksConsumer.processToClaims(jwt).toJwt()

    private fun JwtClaims.toJwt(): JWT = JoseJwtAdapter(this)
}

interface JWT {

    val id: String
    val subject: String
    val claimsAsJson: JSONObject
    val issuerName: Name
    val audienceNames: List<Name>
    val issuedAt: Instant
    val expirationTime: Instant?
    val notBeforeTime: Instant?

    fun hasClaim(name: String): Boolean

    fun getStringListClaimValue(name: String): List<String>

    fun getStringClaimValue(name: String): String
}

private class JoseJwtAdapter(private val delegate: JwtClaims) : JWT {

    override val id: String get() = delegate.jwtId
    override val subject: String get() = delegate.subject
    override val claimsAsJson = delegate.toJson().let(::JSONObject)
    override val issuerName = delegate.issuer.let(::Name)
    override val audienceNames = delegate.audience.map(::Name)
    override val issuedAt: Instant = delegate.issuedAt.let { Instant.fromEpochMilliseconds(it.valueInMillis) }
    override val expirationTime: Instant? = delegate.expirationTime?.let { Instant.fromEpochMilliseconds(it.valueInMillis) }
    override val notBeforeTime: Instant? = delegate.notBefore?.let { Instant.fromEpochMilliseconds(it.valueInMillis) }

    override fun hasClaim(name: String): Boolean = delegate.hasClaim(name)

    override fun getStringListClaimValue(name: String): List<String> = delegate.getStringListClaimValue(name)
    override fun getStringClaimValue(name: String): String = delegate.getStringClaimValue(name)
}

var JwtClaims.issuingTime: Instant
    get() = issuedAt.toInstant()
    set(value) {
        issuedAt = value.toNumericDate()
    }

var JwtClaims.expiryTime: Instant?
    get() = expirationTime?.toInstant()
    set(value) {
        expirationTime = value?.toNumericDate()
    }

var JwtClaims.notBeforeTime: Instant?
    get() = notBefore?.toInstant()
    set(value) {
        notBefore = value?.toNumericDate()
    }

private fun Instant.toNumericDate() = toEpochMilliseconds().let(NumericDate::fromMilliseconds)
private fun NumericDate.toInstant() = valueInMillis.let(Instant::fromEpochMilliseconds)