package org.sollecitom.chassis.openapi.checker.sets

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem.HttpMethod.*
import org.sollecitom.chassis.kotlin.extensions.text.CharacterGroups.letters
import org.sollecitom.chassis.kotlin.extensions.text.CharacterGroups.lowercaseCaseLetters
import org.sollecitom.chassis.openapi.checker.model.OpenApiField
import org.sollecitom.chassis.openapi.checker.model.OpenApiFields
import org.sollecitom.chassis.openapi.checker.rule.OpenApiRule
import org.sollecitom.chassis.openapi.checker.rules.*
import org.sollecitom.chassis.openapi.checker.rules.field.MandatorySuffixTextFieldRule

object StandardOpenApiRules : OpenApiRuleSet {

    private const val MINIMUM_ALLOWED_API_VERSION = 1
    private val textFieldMustEndWithFullStop = MandatorySuffixTextFieldRule(".", true)
    private val versioningPathPrefixRule by lazy { MandatoryVersioningPathPrefixRule(minimumAllowedVersion = MINIMUM_ALLOWED_API_VERSION) }
    private val whitelistedPathAlphabet by lazy { (lowercaseCaseLetters + '-').toSet() }
    private val whitelistedPathParametersAlphabet by lazy { (lowercaseCaseLetters + '-').toSet() }
    private val whitelistedQueryParametersAlphabet by lazy { (lowercaseCaseLetters + '-').toSet() }
    private val whitelistedCookiesAlphabet by lazy { (lowercaseCaseLetters + '-').toSet() }
    private val whitelistedHeadersAlphabet by lazy { (letters + '-').toSet() }
    private val requiredOperationFields by lazy { setOf(OpenApiField("operationId", Operation::getOperationId), OpenApiField("summary", Operation::getSummary)) }

    private val mandatoryRequestBodyRule by lazy { MandatoryRequestBodyRule(methods = setOf(POST to true, PUT to true, PATCH to true)) }
    private val forbiddenRequestBodyRule by lazy { ForbiddenRequestBodyRule(methods = setOf(GET, DELETE, HEAD, TRACE, OPTIONS)) }
    private val pathNameRule by lazy { WhitelistedAlphabetPathNameRule(alphabet = whitelistedPathAlphabet, versioningPathPrefixRule = versioningPathPrefixRule) }
    private val parametersNameRule by lazy { WhitelistedAlphabetParameterNameRule(pathAlphabet = whitelistedPathParametersAlphabet, headerAlphabet = whitelistedHeadersAlphabet, queryAlphabet = whitelistedQueryParametersAlphabet, cookieAlphabet = whitelistedCookiesAlphabet) }
    private val mandatoryRequestBodyContentMediaTypeRule by lazy { MandatoryRequestBodyContentMediaTypesRule(methodsToCheck = setOf(POST, PUT, PATCH)) }
    private val mandatoryRequestBodyDescriptionRule by lazy { MandatoryRequestBodyDescriptionRule(methods = setOf(POST, PUT, PATCH)) }

    private val operationTextFieldRules by lazy {
        CompositeFieldRule(
            mapOf(
                OpenApiFields.Operation.summary to setOf(textFieldMustEndWithFullStop),
                OpenApiFields.Operation.description to setOf(textFieldMustEndWithFullStop)
            )
        )
    }

    override val rules: Set<OpenApiRule> by lazy {
        setOf(
            pathNameRule,
            parametersNameRule,
            DisallowReservedCharactersInParameterNameRule,
            MandatoryOperationFieldsRule(requiredFields = requiredOperationFields),
            EnforceOperationDescriptionDifferentFromSummaryRule,
            EnforceCamelCaseOperationIdRule,
            versioningPathPrefixRule,
            mandatoryRequestBodyRule,
            mandatoryRequestBodyContentMediaTypeRule,
            forbiddenRequestBodyRule,
            mandatoryRequestBodyDescriptionRule,
            operationTextFieldRules
        ) + StandardTracingHeadersOpenApiRules.rules
    }
}