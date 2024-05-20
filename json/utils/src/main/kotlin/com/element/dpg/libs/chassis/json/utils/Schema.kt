package com.element.dpg.libs.chassis.json.utils

import com.github.erosb.jsonsKema.IJsonValue
import com.github.erosb.jsonsKema.JsonParser
import com.github.erosb.jsonsKema.Validator
import org.json.JSONArray
import org.json.JSONObject
import com.github.erosb.jsonsKema.Schema as Skema
import com.github.erosb.jsonsKema.ValidationFailure as SkemaValidationFailure

data class Schema(private val value: Skema, val source: JSONObject) {

    val location: String get() = value.location.getLocation()

    fun validate(json: JSONObject): ValidationFailure? = value.validate(json)?.adapted()

    fun validate(json: JSONArray): ValidationFailure? = value.validate(json)?.adapted()

    private fun Skema.validate(json: IJsonValue) = Validator.forSchema(this).validate(json)

    private fun Skema.validate(json: JSONObject) = validate(JsonParser(json.toString()).parse())

    private fun Skema.validate(json: JSONArray) = validate(JsonParser(json.toString()).parse())

    private fun SkemaValidationFailure.adapted() = ValidationFailure(this)

    data class ValidationFailure(private val failure: SkemaValidationFailure) {

        val message: String get() = failure.toString()
    }
}