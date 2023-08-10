package org.sollecitom.chassis.example.webapp.kweb.starter

import kotlinx.coroutines.coroutineScope
import kweb.*
import kweb.state.KVar
import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.configuration.utils.formatted
import org.sollecitom.chassis.configuration.utils.fromYamlResource
import org.sollecitom.chassis.logger.core.loggable.Loggable

suspend fun main() = coroutineScope<Unit> {

    val environment = rawConfiguration().also(::configureLogging)
    logger.info { environment.formatted() }

    Kweb(port = 9000) {
        doc.body {
            route {
                path("/users/{userId}", PathIdVisualizer("User", "userId"))
                path("/lists/{listId}", PathIdVisualizer("List", "listId"))
                path("", MainPage())
            }
        }
    }
}

interface RoutedComponentTemplate : RoutedComponent {

    fun ElementCreator<*>.render(params: Map<String, KVar<String>>)

    override fun invoke(html: ElementCreator<*>, params: Map<String, KVar<String>>) = with(html) { render(params) }
}

class PathIdVisualizer(private val idType: String, private val listIdParamName: String) : RoutedComponentTemplate {

    override fun ElementCreator<*>.render(params: Map<String, KVar<String>>) {

        val listId = params.getValue(listIdParamName)
        h1().text(listId.map { "$idType id: $it" })
    }
}

typealias RoutedComponent = (ElementCreator<*>, Map<String, KVar<String>>) -> Unit

class MainPage : RoutedComponentTemplate {

    override fun ElementCreator<*>.render(params: Map<String, KVar<String>>) {

        p().text("What is your name?")
        val input = input(type = InputType.text)
        input.value = KVar("Peter Pan")
        val greeting = input.value.map { name -> "Hi $name!" }
        p().text(greeting)
    }
}

private object Starter : Loggable()

private val logger get() = Starter.logger

fun rawConfiguration(): Environment = Environment.JVM_PROPERTIES overrides Environment.ENV overrides Environment.fromYamlResource("default-configuration.yml")