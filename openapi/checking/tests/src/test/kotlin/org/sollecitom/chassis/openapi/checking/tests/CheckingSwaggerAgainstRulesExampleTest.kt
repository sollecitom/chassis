package org.sollecitom.chassis.openapi.checking.tests

import assertk.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.sollecitom.chassis.openapi.checking.checker.checkAgainstRules
import org.sollecitom.chassis.openapi.checking.checker.rules.LowercasePathNameRule
import org.sollecitom.chassis.openapi.checking.test.utils.*

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