package com.element.dpg.libs.chassis.openapi.checking.tests.sets

import assertk.assertThat
import com.element.dpg.libs.chassis.openapi.builder.*
import com.element.dpg.libs.chassis.openapi.checking.checker.checkAgainstRules
import com.element.dpg.libs.chassis.openapi.checking.checker.rules.LowercasePathNameRule
import com.element.dpg.libs.chassis.openapi.checking.test.utils.isNotCompliantWithOnlyViolation
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS

@TestInstance(PER_CLASS)
private class CheckingSwaggerAgainstRulesExampleTest {

    @Test
    fun `applying a path rule on an API that's not compliant`() {

        val path = "/allPeople"
        val api = buildOpenApi {
            path(path) {
                get {
                    operationId("get_people")
                    description("Returns a list of people")
                    responses {
                        status(200) {
                            description("Successful response")
                            content {
                                mediaTypes {
                                    add("text/simple")
                                }
                            }
                        }
                    }
                }
            }
        }

        val result = api.checkAgainstRules(LowercasePathNameRule)

        assertThat(result).isNotCompliantWithOnlyViolation(LowercasePathNameRule.Violation(path))
    }
}