package org.sollecitom.chassis.openapi.checking.checker.sets

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem.HttpMethod.*
import org.sollecitom.chassis.kotlin.extensions.text.CharacterGroups.letters
import org.sollecitom.chassis.kotlin.extensions.text.CharacterGroups.lowercaseCaseLetters
import org.sollecitom.chassis.openapi.checking.checker.model.OpenApiField
import org.sollecitom.chassis.openapi.checking.checker.model.OpenApiFields
import org.sollecitom.chassis.openapi.checking.checker.rule.OpenApiRule
import org.sollecitom.chassis.openapi.checking.checker.rules.*
import org.sollecitom.chassis.openapi.checking.checker.rules.field.MandatorySuffixTextFieldRule

object StandardOpenApiRules : OpenApiRuleSet {

    private val textFieldMustEndWithFullStop = MandatorySuffixTextFieldRule(".", true)
    private val whitelistedPathAlphabet by lazy { (lowercaseCaseLetters + '-').toSet() }
    private val whitelistedPathParametersAlphabet by lazy { (lowercaseCaseLetters + '-').toSet() }
    private val whitelistedQueryParametersAlphabet by lazy { (lowercaseCaseLetters + '-').toSet() }
    private val whitelistedCookiesAlphabet by lazy { (lowercaseCaseLetters + '-').toSet() }
    private val whitelistedHeadersAlphabet by lazy { (letters + '-').toSet() }
    private val requiredOperationFields by lazy { setOf(OpenApiField("operationId", Operation::getOperationId), OpenApiField("summary", Operation::getSummary)) }

    private val versioningPathSegmentRegex = Regex("v[1-9]+\$")
    private val mandatoryRequestBodyRule by lazy { MandatoryRequestBodyRule(methods = setOf(POST to true, PUT to true, PATCH to true)) }
    private val forbiddenRequestBodyRule by lazy { ForbiddenRequestBodyRule(methods = setOf(GET, DELETE, HEAD, TRACE, OPTIONS)) }
    private val pathNameRule by lazy { WhitelistedAlphabetPathNameRule(alphabet = whitelistedPathAlphabet, allowedPathSegments = setOf(versioningPathSegmentRegex)) }
    private val parametersNameRule by lazy { WhitelistedAlphabetParameterNameRule(pathAlphabet = whitelistedPathParametersAlphabet, headerAlphabet = whitelistedHeadersAlphabet, queryAlphabet = whitelistedQueryParametersAlphabet, cookieAlphabet = whitelistedCookiesAlphabet) }
    private val mandatoryRequestBodyContentMediaTypeRule by lazy { MandatoryRequestBodyContentMediaTypesRule(methodsToCheck = setOf(POST, PUT, PATCH)) }
    private val mandatoryRequestBodyDescriptionRule by lazy { MandatoryRequestBodyDescriptionRule(methods = setOf(POST, PUT, PATCH)) }

    private val operationTextFieldRules by lazy {
        FieldSpecificRules(
            OpenApiFields.Operation.summary to setOf(textFieldMustEndWithFullStop),
            OpenApiFields.Operation.description to setOf(textFieldMustEndWithFullStop)
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
            mandatoryRequestBodyRule,
            mandatoryRequestBodyContentMediaTypeRule,
            forbiddenRequestBodyRule,
            mandatoryRequestBodyDescriptionRule,
            operationTextFieldRules
        ) + StandardTracingHeadersOpenApiRules.rules
    }
}