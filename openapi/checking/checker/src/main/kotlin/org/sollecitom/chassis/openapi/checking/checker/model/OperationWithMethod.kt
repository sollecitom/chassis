package org.sollecitom.chassis.openapi.checking.checker.model

import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.PathItem
import io.swagger.v3.oas.models.parameters.Parameter
import io.swagger.v3.oas.models.parameters.RequestBody
import io.swagger.v3.oas.models.responses.ApiResponses

data class OperationWithMethod(val operation: Operation, val method: PathItem.HttpMethod) {

    val parameters: List<Parameter> = operation.parameters ?: emptyList()
    val requestBody: RequestBody? get() = operation.requestBody
    val responses: ApiResponses? get() = operation.responses
}

fun PathItem.operations(pathName: String): Set<OperationWithContext> = operations().asSequence().map { OperationWithContext(it, pathName) }.toSet()

fun PathItem.operations(): Set<OperationWithMethod> {

    val allOperations = mutableSetOf<OperationWithMethod>()

    if (get != null) {
        allOperations.add(OperationWithMethod(get, PathItem.HttpMethod.GET))
    }
    if (put != null) {
        allOperations.add(OperationWithMethod(put, PathItem.HttpMethod.PUT))
    }
    if (head != null) {
        allOperations.add(OperationWithMethod(head, PathItem.HttpMethod.HEAD))
    }
    if (post != null) {
        allOperations.add(OperationWithMethod(post, PathItem.HttpMethod.POST))
    }
    if (delete != null) {
        allOperations.add(OperationWithMethod(delete, PathItem.HttpMethod.DELETE))
    }
    if (patch != null) {
        allOperations.add(OperationWithMethod(patch, PathItem.HttpMethod.PATCH))
    }
    if (options != null) {
        allOperations.add(OperationWithMethod(options, PathItem.HttpMethod.OPTIONS))
    }
    if (trace != null) {
        allOperations.add(OperationWithMethod(trace, PathItem.HttpMethod.TRACE))
    }

    return allOperations
}