package org.sollecitom.chassis.example.event.domain.predicate.search

@JvmInline
value class ProductCode(val value: String) {

    init {
        require(value.isNotBlank()) { "The value must not be blank" }
        // TODO enforce the structure described at https://www.fda.gov/industry/import-program-tools/product-codes-and-product-code-builder
        // TODO add accessors for the various parts e.g., industry, class, subclass, etc.
    }

    companion object
}