package org.sollecitom.chassis.openapi.checking.checker.model

sealed class ParameterLocation(val pathName: String, val operation: OperationWithMethod, val value: String) {

    class Query(pathName: String, operation: OperationWithMethod) : ParameterLocation(pathName, operation, query)
    class Header(pathName: String, operation: OperationWithMethod) : ParameterLocation(pathName, operation, header)
    class Path(pathName: String, operation: OperationWithMethod) : ParameterLocation(pathName, operation, path)
    class Cookie(pathName: String, operation: OperationWithMethod) : ParameterLocation(pathName, operation, cookie)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ParameterLocation

        if (pathName != other.pathName) return false
        if (operation != other.operation) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pathName.hashCode()
        result = 31 * result + operation.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

    override fun toString() = "ParameterLocation(pathName='$pathName', operation=$operation, value='$value')"

    companion object {

        const val query = "query"
        const val path = "path"
        const val header = "header"
        const val cookie = "cookie"

        fun valueOf(pathName: String, operation: OperationWithMethod, value: String): ParameterLocation = when (value) {
            query -> Query(pathName, operation)
            header -> Header(pathName, operation)
            path -> Path(pathName, operation)
            cookie -> Cookie(pathName, operation)
            else -> error("Unknown parameter type $value")
        }
    }
}