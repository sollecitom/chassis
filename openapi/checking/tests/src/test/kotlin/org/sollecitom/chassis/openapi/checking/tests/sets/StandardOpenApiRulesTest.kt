package org.sollecitom.chassis.openapi.checking.tests.sets

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.swagger.v3.oas.models.PathItem.HttpMethod
import io.swagger.v3.oas.models.PathItem.HttpMethod.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.openapi.checking.checker.model.ParameterLocation
import org.sollecitom.chassis.openapi.checking.checker.rules.DisallowReservedCharactersInParameterNameRule
import org.sollecitom.chassis.openapi.checking.checker.rules.MandatoryVersioningPathPrefixRule
import org.sollecitom.chassis.openapi.checking.checker.rules.WhitelistedAlphabetParameterNameRule
import org.sollecitom.chassis.openapi.checking.checker.rules.WhitelistedAlphabetPathNameRule
import org.sollecitom.chassis.openapi.checking.checker.sets.StandardOpenApiRules
import org.sollecitom.chassis.openapi.checking.checker.sets.checkAgainstRules
import org.sollecitom.chassis.openapi.checking.test.utils.*

@TestInstance(PER_CLASS)
private class StandardOpenApiRulesTest : OpenApiTestSpecification {

    override val validPath = "/v1/people/{person-id}/close-friends"
    override val validOperationId = "getCloseFriends"
    override val validSummary = "Returns a list of close friends for the given person."

    @Nested
    @TestInstance(PER_CLASS)
    inner class Paths {

        @Test
        fun `valid value works`() {

            val path = validPath
            val api = buildOpenApi {
                path(path)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isCompliant()
        }

        @Test
        fun `cannot contain numbers`() {

            val invalidPath = "/v2/all123"
            val api = buildOpenApi {
                path(invalidPath)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetPathNameRule.Violation> { violation ->
                assertThat(violation.path).isEqualTo(invalidPath)
            }
        }

        @Test
        fun `cannot contain uppercase letters`() {

            val invalidPath = "/v1/All"
            val api = buildOpenApi {
                path(invalidPath)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetPathNameRule.Violation> { violation ->
                assertThat(violation.path).isEqualTo(invalidPath)
            }
        }

        @Test
        fun `cannot contain symbols`() {

            val invalidPath = "/v1/all_people"
            val api = buildOpenApi {
                path(invalidPath)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetPathNameRule.Violation> { violation ->
                assertThat(violation.path).isEqualTo(invalidPath)
            }
        }

        @Test
        fun `templated segments cannot contain numbers`() {

            val invalidPath = "/v1/all/{id123}"
            val api = buildOpenApi {
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
            val api = buildOpenApi {
                path(invalidPath)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetPathNameRule.Violation> { violation ->
                assertThat(violation.path).isEqualTo(invalidPath)
            }
        }

        @Test
        fun `valid templated segments work`() {

            val path = "/v1/people/{person-id}"
            val api = buildOpenApi {
                path(path)
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isCompliant()
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Parameters { // TODO maybe use a test specification to test all parameter types

        @Nested
        inner class Query {

            private val validParameterName = "some-filter"
            private val parameterLocation = ParameterLocation.query

            @Test
            fun `valid value works`() {

                val parameterName = validParameterName
                val api = buildOpenApi {
                    path(validPath) {
                        get {
                            operationId = validOperationId
                            summary = validSummary
                            parameters {
                                add {
                                    name = parameterName
                                    `in` = parameterLocation
                                    required = false
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isCompliant()
            }

            @Test
            fun `cannot contain numbers`() {

                val invalidParameterName = "filter123"
                val api = buildOpenApi {
                    path(validPath) {
                        get {
                            operationId = validOperationId
                            summary = validSummary
                            parameters {
                                add {
                                    name = invalidParameterName
                                    `in` = parameterLocation
                                    required = false
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation> { violation ->
                    assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
                    assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                    assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                    assertThat(violation.parameter.location.operation.method).isEqualTo(GET)
                }
            }

            @Test
            fun `cannot contain symbols`() {

                val invalidParameterName = "filte_r"
                val api = buildOpenApi {
                    path(validPath) {
                        get {
                            operationId = validOperationId
                            summary = validSummary
                            parameters {
                                add {
                                    name = invalidParameterName
                                    `in` = parameterLocation
                                    required = false
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation> { violation ->
                    assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
                    assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                    assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                    assertThat(violation.parameter.location.operation.method).isEqualTo(GET)
                }
            }

            @Test
            fun `cannot contain uppercase letters`() {

                val invalidParameterName = "fiLter"
                val api = buildOpenApi {
                    path(validPath) {
                        post {
                            operationId = validOperationId
                            summary = validSummary
                            setValidRequestBody()
                            parameters {
                                add {
                                    name = invalidParameterName
                                    `in` = parameterLocation
                                    required = false
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation> { violation ->
                    assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
                    assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                    assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                    assertThat(violation.parameter.location.operation.method).isEqualTo(POST)
                }
            }

            @Test
            fun `cannot allow reserved URL characters`() {

                val parameterName = validParameterName
                val api = buildOpenApi {
                    path(validPath) {
                        put {
                            operationId = validOperationId
                            summary = validSummary
                            setValidRequestBody()
                            parameters {
                                add {
                                    name = parameterName
                                    `in` = parameterLocation
                                    required = false
                                    allowReserved = true
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isNotCompliantWithOnlyViolation<DisallowReservedCharactersInParameterNameRule.Violation> { violation ->
                    assertThat(violation.parameter.name).isEqualTo(parameterName)
                    assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                    assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                    assertThat(violation.parameter.location.operation.method).isEqualTo(PUT)
                }
            }
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Path {

            private val validParameterName = "some-id"
            private val parameterLocation = ParameterLocation.path

            @Test
            fun `valid value works`() {

                val parameterName = validParameterName
                val api = buildOpenApi {
                    path(validPath) {
                        get {
                            operationId = validOperationId
                            summary = validSummary
                            parameters {
                                add {
                                    name = parameterName
                                    `in` = parameterLocation
                                    required = false
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isCompliant()
            }

            @Test
            fun `cannot contain numbers`() {

                val invalidParameterName = "id123"
                val api = buildOpenApi {
                    path(validPath) {
                        get {
                            operationId = validOperationId
                            summary = validSummary
                            parameters {
                                add {
                                    name = invalidParameterName
                                    `in` = parameterLocation
                                    required = false
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation> { violation ->
                    assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
                    assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                    assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                    assertThat(violation.parameter.location.operation.method).isEqualTo(GET)
                }
            }

            @Test
            fun `cannot contain symbols`() {

                val invalidParameterName = "i_d"
                val api = buildOpenApi {
                    path(validPath) {
                        get {
                            operationId = validOperationId
                            summary = validSummary
                            parameters {
                                add {
                                    name = invalidParameterName
                                    `in` = parameterLocation
                                    required = false
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation> { violation ->
                    assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
                    assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                    assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                    assertThat(violation.parameter.location.operation.method).isEqualTo(GET)
                }
            }

            @Test
            fun `cannot contain uppercase letters`() {

                val invalidParameterName = "ID"
                val api = buildOpenApi {
                    path(validPath) {
                        post {
                            operationId = validOperationId
                            summary = validSummary
                            setValidRequestBody()
                            parameters {
                                add {
                                    name = invalidParameterName
                                    `in` = parameterLocation
                                    required = false
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation> { violation ->
                    assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
                    assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                    assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                    assertThat(violation.parameter.location.operation.method).isEqualTo(POST)
                }
            }

            @Test
            fun `cannot allow reserved URL characters`() {

                val parameterName = validParameterName
                val api = buildOpenApi {
                    path(validPath) {
                        put {
                            operationId = validOperationId
                            summary = validSummary
                            setValidRequestBody()
                            parameters {
                                add {
                                    name = parameterName
                                    `in` = parameterLocation
                                    required = false
                                    allowReserved = true
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isNotCompliantWithOnlyViolation<DisallowReservedCharactersInParameterNameRule.Violation> { violation ->
                    assertThat(violation.parameter.name).isEqualTo(parameterName)
                    assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                    assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                    assertThat(violation.parameter.location.operation.method).isEqualTo(PUT)
                }
            }
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Cookies {

            private val validParameterName = "some-session-id"
            private val parameterLocation = ParameterLocation.path

            @Test
            fun `valid value works`() {

                val parameterName = validParameterName
                val api = buildOpenApi {
                    path(validPath) {
                        get {
                            operationId = validOperationId
                            summary = validSummary
                            parameters {
                                add {
                                    name = parameterName
                                    `in` = parameterLocation
                                    required = false
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isCompliant()
            }

            @Test
            fun `cannot contain numbers`() {

                val invalidParameterName = "session-123"
                val api = buildOpenApi {
                    path(validPath) {
                        get {
                            operationId = validOperationId
                            summary = validSummary
                            parameters {
                                add {
                                    name = invalidParameterName
                                    `in` = parameterLocation
                                    required = false
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation> { violation ->
                    assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
                    assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                    assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                    assertThat(violation.parameter.location.operation.method).isEqualTo(GET)
                }
            }

            @Test
            fun `cannot contain symbols`() {

                val invalidParameterName = "session_id"
                val api = buildOpenApi {
                    path(validPath) {
                        get {
                            operationId = validOperationId
                            summary = validSummary
                            parameters {
                                add {
                                    name = invalidParameterName
                                    `in` = parameterLocation
                                    required = false
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation> { violation ->
                    assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
                    assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                    assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                    assertThat(violation.parameter.location.operation.method).isEqualTo(GET)
                }
            }

            @Test
            fun `cannot contain uppercase letters`() {

                val invalidParameterName = "SESSION-ID"
                val api = buildOpenApi {
                    path(validPath) {
                        post {
                            operationId = validOperationId
                            summary = validSummary
                            setValidRequestBody()
                            parameters {
                                add {
                                    name = invalidParameterName
                                    `in` = parameterLocation
                                    required = false
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation> { violation ->
                    assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
                    assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                    assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                    assertThat(violation.parameter.location.operation.method).isEqualTo(POST)
                }
            }

            @Test
            fun `cannot allow reserved URL characters`() {

                val parameterName = validParameterName
                val api = buildOpenApi {
                    path(validPath) {
                        put {
                            operationId = validOperationId
                            summary = validSummary
                            setValidRequestBody()
                            parameters {
                                add {
                                    name = parameterName
                                    `in` = parameterLocation
                                    required = false
                                    allowReserved = true
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isNotCompliantWithOnlyViolation<DisallowReservedCharactersInParameterNameRule.Violation> { violation ->
                    assertThat(violation.parameter.name).isEqualTo(parameterName)
                    assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                    assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                    assertThat(violation.parameter.location.operation.method).isEqualTo(PUT)
                }
            }
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Headers {

            private val validParameterName = "SoME-HeadER"
            private val parameterLocation = ParameterLocation.header

            @Test
            fun `valid value works`() {

                val parameterName = validParameterName
                val api = buildOpenApi {
                    path(validPath) {
                        get {
                            operationId = validOperationId
                            summary = validSummary
                            parameters {
                                add {
                                    name = parameterName
                                    `in` = parameterLocation
                                    required = false
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isCompliant()
            }

            @Test
            fun `cannot contain numbers`() {

                val invalidParameterName = "HEADER-123"
                val api = buildOpenApi {
                    path(validPath) {
                        get {
                            operationId = validOperationId
                            summary = validSummary
                            parameters {
                                add {
                                    name = invalidParameterName
                                    `in` = parameterLocation
                                    required = false
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation> { violation ->
                    assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
                    assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                    assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                    assertThat(violation.parameter.location.operation.method).isEqualTo(GET)
                }
            }

            @Test
            fun `cannot contain symbols`() {

                val invalidParameterName = "HEA_DER"
                val api = buildOpenApi {
                    path(validPath) {
                        get {
                            operationId = validOperationId
                            summary = validSummary
                            parameters {
                                add {
                                    name = invalidParameterName
                                    `in` = parameterLocation
                                    required = false
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isNotCompliantWithOnlyViolation<WhitelistedAlphabetParameterNameRule.Violation> { violation ->
                    assertThat(violation.parameter.name).isEqualTo(invalidParameterName)
                    assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                    assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                    assertThat(violation.parameter.location.operation.method).isEqualTo(GET)
                }
            }

            @Test
            fun `cannot allow reserved URL characters`() {

                val parameterName = validParameterName
                val api = buildOpenApi {
                    path(validPath) {
                        put {
                            operationId = validOperationId
                            summary = validSummary
                            setValidRequestBody()
                            parameters {
                                add {
                                    name = parameterName
                                    `in` = parameterLocation
                                    required = false
                                    allowReserved = true
                                }
                            }
                        }
                    }
                }

                val result = api.checkAgainstRules(StandardOpenApiRules)

                assertThat(result).isNotCompliantWithOnlyViolation<DisallowReservedCharactersInParameterNameRule.Violation> { violation ->
                    assertThat(violation.parameter.name).isEqualTo(parameterName)
                    assertThat(violation.parameter.location.value).isEqualTo(parameterLocation)
                    assertThat(violation.parameter.location.pathName).isEqualTo(validPath)
                    assertThat(violation.parameter.location.operation.method).isEqualTo(PUT)
                }
            }
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
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Put : MethodTestSpecification.WithMandatoryRequestBody {

            override val method = PUT
            override val validPath get() = this@StandardOpenApiRulesTest.validPath
            override val validOperationId get() = this@StandardOpenApiRulesTest.validOperationId
            override val validSummary get() = this@StandardOpenApiRulesTest.validSummary
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Patch : MethodTestSpecification.WithMandatoryRequestBody {

            override val method = PATCH
            override val validPath get() = this@StandardOpenApiRulesTest.validPath
            override val validOperationId get() = this@StandardOpenApiRulesTest.validOperationId
            override val validSummary get() = this@StandardOpenApiRulesTest.validSummary
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Get : MethodTestSpecification.WithoutRequestBody {

            override val method = GET
            override val validPath get() = this@StandardOpenApiRulesTest.validPath
            override val validOperationId get() = this@StandardOpenApiRulesTest.validOperationId
            override val validSummary get() = this@StandardOpenApiRulesTest.validSummary
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Options : MethodTestSpecification.WithoutRequestBody {

            override val method = OPTIONS
            override val validPath get() = this@StandardOpenApiRulesTest.validPath
            override val validOperationId get() = this@StandardOpenApiRulesTest.validOperationId
            override val validSummary get() = this@StandardOpenApiRulesTest.validSummary
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Delete : MethodTestSpecification.WithoutRequestBody {

            override val method = DELETE
            override val validPath get() = this@StandardOpenApiRulesTest.validPath
            override val validOperationId get() = this@StandardOpenApiRulesTest.validOperationId
            override val validSummary get() = this@StandardOpenApiRulesTest.validSummary
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Head : MethodTestSpecification.WithoutRequestBody {

            override val method = HEAD
            override val validPath get() = this@StandardOpenApiRulesTest.validPath
            override val validOperationId get() = this@StandardOpenApiRulesTest.validOperationId
            override val validSummary get() = this@StandardOpenApiRulesTest.validSummary
        }

        @Nested
        @TestInstance(PER_CLASS)
        inner class Trace : MethodTestSpecification.WithoutRequestBody {

            override val method = TRACE
            override val validPath get() = this@StandardOpenApiRulesTest.validPath
            override val validOperationId get() = this@StandardOpenApiRulesTest.validOperationId
            override val validSummary get() = this@StandardOpenApiRulesTest.validSummary
        }
    }

    @Nested
    @TestInstance(PER_CLASS)
    inner class Versioning {

        @Test
        fun `cannot omit versioning path prefix`() {

            val path = "/an-almost-valid-path"
            val api = buildOpenApi {
                path(path) {
                    post {
                        operationId = validOperationId
                        summary = validSummary
                        setValidRequestBody()
                    }
                }
            }

            val result = api.checkAgainstRules(StandardOpenApiRules)

            assertThat(result).isNotCompliantWithOnlyViolation<MandatoryVersioningPathPrefixRule.Violation> { violation ->
                assertThat(violation.pathName).isEqualTo(path)
            }
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

// TODO generic
//    // TODO investigate using dictionaries to whitelist words and structure e.g. plural vs singular (like with https://dev.languagetool.org/java-api)
//    // TODO media types
//        // TODO write rule to enforce media type names conventions for all operations that specify one
//        // TODO write rule to enforce mandatory media type examples for all operations that specify one

// TODO requests
//    // TODO check mandatory request headers (e.g. tracing and idempotency headers, authentication headers)
//    // TODO (maybe) check that the optional "sort" parameter is like `sort=+transactionDate,-merchantName` (+ = ascending, - = descending) (check a sort schema is referenced)
//    // TODO check that if pagination exists, it's implemented correctly (`nextPageToken` as optional header passed and returned, optional `nextPageMaxSize` (default value))

// TODO responses
//    // TODO all methods should specify their Produces type(s)
//    // TODO check mandatory response headers
//    // TODO check that every retrieved resource contains a created-at field, with the right schema
//    // TODO check that if a retrieved resource contains an updated-at field, it has the right schema
//    // TODO whitelist response codes
//    // TODO check 4xx and 5xx responses reference the right schema for the response body
//    // TODO check that if pagination exists, it's implemented correctly (`nextPageToken` as optional header passed and returned, optional `nextPageMaxSize` (default value))