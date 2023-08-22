package org.sollecitom.chassis.openapi.validation.request.validator.test.utils

import assertk.Assert
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.atlassian.oai.validator.report.SimpleValidationReportFormat
import com.atlassian.oai.validator.report.ValidationReport
import org.sollecitom.chassis.openapi.validation.request.validator.ValidationReportError
import org.sollecitom.chassis.openapi.validation.request.validator.errors

fun Assert<ValidationReport>.hasNoErrors(printErrors: Boolean = false) = given { report ->

    if (printErrors && report.hasErrors()) {
        println(SimpleValidationReportFormat.getInstance().apply(report))
    }
    assertThat(report.hasErrors()).isFalse()
}

fun Assert<ValidationReport>.hasErrors(printErrors: Boolean = false, count: Int? = null) = given { report ->

    if (printErrors && report.hasErrors()) {
        println(SimpleValidationReportFormat.getInstance().apply(report))
    }
    assertThat(report.hasErrors()).isTrue()
    if (count != null) {
        assertThat(report.errors).hasSize(count)
    }
}

fun Assert<ValidationReport>.hasExactlyOneErrorWithKey(errorKey: String, printErrors: Boolean = false) = given { report ->

    assertThat(report).hasErrors(printErrors = printErrors, count = 1)
    assertThat(report.errors.single().key).isEqualTo(errorKey)
}

fun Assert<ValidationReport>.containsOnly(error: ValidationReportError, printErrors: Boolean = false) = given { report ->

    assertThat(report).hasExactlyOneErrorWithKey(error.key, printErrors)
}

