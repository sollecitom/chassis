package org.sollecitom.chassis.correlation.core.domain.access.scope

class AccessScope(private val containerStack: List<AccessContainer> = emptyList()) {

    val containers get() = containerStack

    init {
        require(containerStack.distinct() == containerStack) { "No duplicates are allowed in the container stack for access scope" }
    }

    fun includesContainer(container: AccessContainer): Boolean = containerStack.any { it == container }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AccessScope) return false

        if (containerStack != other.containerStack) return false

        return true
    }

    override fun hashCode() = containerStack.hashCode()

    override fun toString() = "AccessScope(containerStack=$containerStack)"

    companion object
}