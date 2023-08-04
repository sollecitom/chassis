package org.sollecitom.chassis.example.webapp.htmx.starter

import kotlinx.html.*
import kotlinx.html.stream.createHTML

object HTMX {
    const val version = "1.9.4"

    operator fun invoke(html: BODY.() -> Unit): String {
        return "<!DOCTYPE html>" + createHTML().html {
            lang = "en"
            head {
                script { src = "/htmx.org/$version/dist/htmx.js" }
                link { href = "/modest-variation.css"; rel = "stylesheet" }
            }
            body {
                html()
            }
        }
    }
}

fun HTMLTag.hxGet(value: String) = setAttribute("hx-get", value)

fun HTMLTag.hxSwap(value: String) = setAttribute("hx-swap", value)

fun HTMLTag.hxTarget(value: String) = setAttribute("hx-target", value)

private fun HTMLTag.setAttribute(name: String, value: String) {

    attributes += name to value
}