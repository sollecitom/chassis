package com.element.dpg.libs.chassis.openapi.checking.tests.sets.sets

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEqualTo
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.PathItem.HttpMethod.*
import io.swagger.v3.oas.models.SpecVersion
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import com.element.dpg.libs.chassis.logger.core.JvmLoggerFactory
import com.element.dpg.libs.chassis.logging.standard.configuration.StandardLoggingConfiguration
import com.element.dpg.libs.chassis.logging.standard.configuration.applyTo
import com.element.dpg.libs.chassis.openapi.builder.OpenApiBuilder
import com.element.dpg.libs.chassis.openapi.builder.buildOpenApi
import com.element.dpg.libs.chassis.openapi.builder.post
import com.element.dpg.libs.chassis.openapi.checking.checker.model.OpenApiFields
import com.element.dpg.libs.chassis.openapi.checking.checker.model.ParameterLocation
import com.element.dpg.libs.chassis.openapi.checking.checker.rules.MandatoryInfoFieldsRule
import com.element.dpg.libs.chassis.openapi.checking.checker.rules.WhitelistedAlphabetPathNameRule
import com.element.dpg.libs.chassis.openapi.checking.checker.rules.WhitelistedOpenApiVersionFieldRule
import com.element.dpg.libs.chassis.openapi.checking.checker.sets.StandardOpenApiRules
import com.element.dpg.libs.chassis.openapi.checking.checker.sets.checkAgainstRules
import com.element.dpg.libs.chassis.openapi.checking.test.utils.isCompliant
import com.element.dpg.libs.chassis.openapi.checking.test.utils.isNotCompliantWithOnlyViolation

@TestInstance(PER_CLASS)
private class StandardOpenApiRulesTest : OpenApiTestSpecification {

    override val validPath = "/people/{person-id}/close-friends"
    override val validOperationId = "getCloseFriends"
    override val validSummary = "Returns a list of close friends for the given person."

    init {
        StandardLoggingConfiguration(defaultMinimumLoggingLevel = com.element.dpg.libs.chassis.logger.core.LoggingLevel.INFO).applyTo(JvmLoggerFactory)
    }

    override fun openApi(version: OpenApiBuilder.OpenApiVersion, customize: OpenApiBuilder.() -> Unit): OpenAPI {

        val prepare: OpenApiBuilder.() -> Unit = {
            version(version)
            info {
                title = "Some title"
                description = "A description."
            }

            customize()
        }
        return buildOpenApi(SpecVersion.V31, prepare)
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Info {

        @Test
        fun `cannot specify a disallowed OpenAPI version`() {

            val disallowedOpenApiVersion = "3.0.0"
            val api = openApi {
                version(disallowedOpenApiVersion)
                path("/something")
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedOpenApiVersionFieldRule.Violation> { violation ->
                assertThat(violation.declaredVersion).isEqualTo(disallowedOpenApiVersion)
            }
        }

        @Test
        fun `cannot omit a title`() {

            val api = openApi {
                info {
                    title = null
                }
                path("/something")
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryInfoFieldsRule.Violation> { violation ->
                assertThat(violation.missingRequiredFields).containsOnly(OpenApiFields.Info.title)
            }
        }

        @Test
        fun `cannot omit a description`() {

            val api = openApi {
                info {
                    description = null
                }
                path("/something")
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryInfoFieldsRule.Violation> { violation ->
                assertThat(violation.missingRequiredFields).containsOnly(OpenApiFields.Info.description)
            }
        }
        // TODO add test for description ending in full stop
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Paths {

        @Test
        fun `valid value works`() {

            val path = validPath
            val api = openApi {
                path(path)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isCompliant()
        }

        @Test
        fun `trailing version path segment with number works`() {

            val path = "/commands/register-user/v1"
            val api = openApi {
                path(path)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isCompliant()
        }

        @Test
        fun `cannot contain trailing invalid path segment that looks like a version with number`() {

            val invalidPath = "/commands/register-user/av1"
            val api = openApi {
                path(invalidPath)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetPathNameRule.Violation> { violation ->
                assertThat(violation.path).isEqualTo(invalidPath)
            }
        }

        @Test
        fun `cannot contain numbers`() {

            val invalidPath = "/commands/123/register-user"
            val api = openApi {
                path(invalidPath)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetPathNameRule.Violation> { violation ->
                assertThat(violation.path).isEqualTo(invalidPath)
            }
        }

        @Test
        fun `leading version path segment with number works`() {

            val path = "/v1/commands/register-user"
            val api = openApi {
                path(path)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isCompliant()
        }

        @Test
        fun `cannot contain uppercase letters`() {

            val invalidPath = "/something/All"
            val api = openApi {
                path(invalidPath)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetPathNameRule.Violation> { violation ->
                assertThat(violation.path).isEqualTo(invalidPath)
            }
        }

        @Test
        fun `cannot contain symbols`() {

            val invalidPath = "/something/all_people"
            val api = openApi {
                path(invalidPath)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetPathNameRule.Violation> { violation ->
                assertThat(violation.path).isEqualTo(invalidPath)
            }
        }

        @Test
        fun `templated segments cannot contain numbers`() {

            val invalidParameterName = "id123"
            val invalidPath = "/v1/all/{$invalidParameterName}"
            val api = openApi {
                path(invalidPath)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetPathNameRule.Violation> { violation ->
                assertThat(violation.path).isEqualTo(invalidPath)
            }
        }

        @Test
        fun `templated segments cannot contain further braces`() {

            val invalidPath = "/v1/all/{{a}"
            val api = openApi {
                path(invalidPath)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetPathNameRule.Violation> { violation ->
                assertThat(violation.path).isEqualTo(invalidPath)
            }
        }

        @Test
        fun `valid templated segments work`() {

            val path = "/people/{person-id}"
            val api = openApi {
                path(path)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isCompliant()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Parameters {

        @Nested
        inner class Query : OperationParametersTestSpecification.WithDisallowedUppercaseLetters, OpenApiTestSpecification by this@StandardOpenApiRulesTest {

            override val validParameterName = "some-filter"
            override val parameterLocation = ParameterLocation.query
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Path : OperationParametersTestSpecification.WithDisallowedUppercaseLetters, OpenApiTestSpecification by this@StandardOpenApiRulesTest {

            override val validParameterName = "some-id"
            override val parameterLocation = ParameterLocation.path
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Cookies : OperationParametersTestSpecification.WithDisallowedUppercaseLetters, OpenApiTestSpecification by this@StandardOpenApiRulesTest {

            override val validParameterName = "some-session-id"
            override val parameterLocation = ParameterLocation.path
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Headers : OperationParametersTestSpecification, OpenApiTestSpecification by this@StandardOpenApiRulesTest {

            override val validParameterName = "SoME-HeadER"
            override val parameterLocation = ParameterLocation.header
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Operations {

        @Nested
        @TestInstance(PER_CLASS)
        inner class Post : MethodTestSpecification.WithMandatoryRequestBody {

            override val method = POST
            override val validPath get() = this@StandardOpenApiRulesTest.validPath
            override val validOperationId get() = this@StandardOpenApiRulesTest.validOperationId
            override val validSummary get() = this@StandardOpenApiRulesTest.validSummary

            override fun openApi(version: OpenApiBuilder.OpenApiVersion, customize: OpenApiBuilder.() -> Unit) = this@StandardOpenApiRulesTest.openApi(version, customize)
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Put : MethodTestSpecification.WithMandatoryRequestBody {

            override val method = PUT
            override val validPath get() = this@StandardOpenApiRulesTest.validPath
            override val validOperationId get() = this@StandardOpenApiRulesTest.validOperationId
            override val validSummary get() = this@StandardOpenApiRulesTest.validSummary

            override fun openApi(version: OpenApiBuilder.OpenApiVersion, customize: OpenApiBuilder.() -> Unit) = this@StandardOpenApiRulesTest.openApi(version, customize)
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Patch : MethodTestSpecification.WithMandatoryRequestBody {

            override val method = PATCH
            override val validPath get() = this@StandardOpenApiRulesTest.validPath
            override val validOperationId get() = this@StandardOpenApiRulesTest.validOperationId
            override val validSummary get() = this@StandardOpenApiRulesTest.validSummary

            override fun openApi(version: OpenApiBuilder.OpenApiVersion, customize: OpenApiBuilder.() -> Unit) = this@StandardOpenApiRulesTest.openApi(version, customize)
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Get : MethodTestSpecification.WithoutRequestBody {

            override val method = GET
            override val validPath get() = this@StandardOpenApiRulesTest.validPath
            override val validOperationId get() = this@StandardOpenApiRulesTest.validOperationId
            override val validSummary get() = this@StandardOpenApiRulesTest.validSummary

            override fun openApi(version: OpenApiBuilder.OpenApiVersion, customize: OpenApiBuilder.() -> Unit) = this@StandardOpenApiRulesTest.openApi(version, customize)
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Options : MethodTestSpecification.WithoutRequestBody {

            override val method = OPTIONS
            override val validPath get() = this@StandardOpenApiRulesTest.validPath
            override val validOperationId get() = this@StandardOpenApiRulesTest.validOperationId
            override val validSummary get() = this@StandardOpenApiRulesTest.validSummary

            override fun openApi(version: OpenApiBuilder.OpenApiVersion, customize: OpenApiBuilder.() -> Unit) = this@StandardOpenApiRulesTest.openApi(version, customize)
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Delete : MethodTestSpecification.WithoutRequestBody {

            override val method = DELETE
            override val validPath get() = this@StandardOpenApiRulesTest.validPath
            override val validOperationId get() = this@StandardOpenApiRulesTest.validOperationId
            override val validSummary get() = this@StandardOpenApiRulesTest.validSummary

            override fun openApi(version: OpenApiBuilder.OpenApiVersion, customize: OpenApiBuilder.() -> Unit) = this@StandardOpenApiRulesTest.openApi(version, customize)
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Head : MethodTestSpecification.WithoutRequestBody {

            override val method = HEAD
            override val validPath get() = this@StandardOpenApiRulesTest.validPath
            override val validOperationId get() = this@StandardOpenApiRulesTest.validOperationId
            override val validSummary get() = this@StandardOpenApiRulesTest.validSummary

            override fun openApi(version: OpenApiBuilder.OpenApiVersion, customize: OpenApiBuilder.() -> Unit) = this@StandardOpenApiRulesTest.openApi(version, customize)
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Trace : MethodTestSpecification.WithoutRequestBody {

            override val method = TRACE
            override val validPath get() = this@StandardOpenApiRulesTest.validPath
            override val validOperationId get() = this@StandardOpenApiRulesTest.validOperationId
            override val validSummary get() = this@StandardOpenApiRulesTest.validSummary

            override fun openApi(version: OpenApiBuilder.OpenApiVersion, customize: OpenApiBuilder.() -> Unit) = this@StandardOpenApiRulesTest.openApi(version, customize)
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Versioning {

        @Test
        fun `can omit a versioning path prefix`() {

            val path = "/a-valid-path-without-versioning-prefix"
            val api = openApi {
                path(path) {
                    post {
                        operationId = validOperationId
                        summary = validSummary
                        setValidRequestBody()
                    }
                }
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isCompliant()
        }

        @Test
        fun `can include a versioning path prefix`() {

            val path = "/v1/a-valid-path-with-versioning-prefix"
            val api = openApi {
                path(path) {
                    post {
                        operationId = validOperationId
                        summary = validSummary
                        setValidRequestBody()
                    }
                }
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isCompliant()
        }
    }

//    @Test
//    fun `spell checking PoC`() {
//
//        val languageAnalyser = JLanguageTool(BritishEnglish())
//        val aWordWithATypo = "typpo"
//        val aSingularWord = "person"
//        val aPluralWord = "people"
//
//        val analyzedSingularWord = languageAnalyser.analyzeWord(aSingularWord)
//        println("Word '$aSingularWord' is singular: ${analyzedSingularWord.isSingular}")
//
//        val analyzedPluralWord = languageAnalyser.analyzeWord(aPluralWord)
//        println("Word '$aPluralWord' is singular: ${analyzedPluralWord.isSingular}")
//
////        val issues = languageAnalyser.check(aWordWithATypo)
////        val issue = issues.single()
////
////        println(issue.message)
////        println("From ${issue.patternFromPos} to ${issue.patternToPos}")
////        val wordTokens = issue.sentence.tokensWithoutWhitespace.filter { it.isAWord }
////        val word = wordTokens.single()
////        println(word)
//    }
}

//fun JLanguageTool.analyzeWord(word: String): Word {
//
//    require(word.isNotBlank()) { "Argument cannot be blank" }
//    require(word.trim() == word) { "Argument cannot start or end with whitespace" }
//    require(word.split(" ").size == 1) { "Argument cannot contain whitespace" }
//    val analysedSentence = getAnalyzedSentence(word)
//    val analysedWord = analysedSentence.tokensWithoutWhitespace.single { it.isAWord }
//    return WordAdapter(analysedWord)
//}
//
//private class WordAdapter(private val readings: AnalyzedTokenReadings) : Word {
//    override val value: String
//        get() = readings.token
//    override val isSingular: Boolean
//        get() = readings.isASingularWord
//}

//interface Word {
//
//    val value: String
//    val isSingular: Boolean
//}
//
//private val AnalyzedTokenReadings.isASingularWord: Boolean
//    get() = isAWord && chunkTags.isNotEmpty() && chunkTags.any { "singular" in it.chunkTag }
//
//private val AnalyzedTokenReadings.isAPluralWord: Boolean
//    get() = isAWord && chunkTags.isNotEmpty() && chunkTags.any { "plural" in it.chunkTag }
//
//private val AnalyzedTokenReadings.isAWord: Boolean
//    get() = when {
//        isSentenceStart -> false
//        isFieldCode -> false
//        isLinebreak -> false
//        isWhitespaceBefore -> false
//        else -> true
//    }

// TODO implement the additional test cases
// sections
//    add mandatory info fields, including version
// generic
//    // investigate using dictionaries to whitelist words and structure e.g. plural vs singular (like with https://dev.languagetool.org/java-api)
//    // media types
//        // write rule to enforce media type names conventions for all operations that specify one
//        // write rule to enforce mandatory media type examples for all operations that specify one

// requests
//    // check mandatory request headers (e.g. tracing and idempotency headers, authentication headers)
//    // (maybe) check that the optional "sort" parameter is like `sort=+transactionDate,-merchantName` (+ = ascending, - = descending) (check a sort schema is referenced)
//    // check that if pagination exists, it's implemented correctly (`nextPageToken` as optional header passed and returned, optional `nextPageMaxSize` (default value))

// responses
//    // all methods should specify their Produces type(s)
//    // check mandatory response headers
//    // check that every retrieved resource contains a created-at field, with the right schema
//    // check that if a retrieved resource contains an updated-at field, it has the right schema
//    // whitelist response codes
//    // check 4xx and 5xx responses reference the right schema for the response body
//    // check that if pagination exists, it's implemented correctly (`nextPageToken` as optional header passed and returned, optional `nextPageMaxSize` (default value))

