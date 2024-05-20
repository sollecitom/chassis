package com.element.dpg.libs.chassis.openapi.validation.request.validator

import com.atlassian.oai.validator.report.ValidationReport
import com.atlassian.oai.validator.report.ValidationReport.Level.ERROR

val ValidationReport.errors: List<ValidationReport.Message> get() = messages.filter { it.level == ERROR }