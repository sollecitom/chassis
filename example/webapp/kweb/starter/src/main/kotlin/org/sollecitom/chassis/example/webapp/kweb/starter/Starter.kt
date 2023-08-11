package org.sollecitom.chassis.example.webapp.kweb.starter

import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.JsonPrimitive
import kweb.*
import kweb.plugins.fomanticUI.fomantic
import kweb.plugins.fomanticUI.fomanticUIPlugin
import kweb.routing.RouteReceiver
import kweb.state.KVar
import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.configuration.utils.formatted
import org.sollecitom.chassis.configuration.utils.fromYamlResource
import org.sollecitom.chassis.logger.core.loggable.Loggable

suspend fun main() = coroutineScope<Unit> {

    val environment = rawConfiguration().also(::configureLogging)
    logger.info { environment.formatted() }

    Kweb(port = 9000, plugins = listOf(fomanticUIPlugin)) {
        doc.body {
            route {
                path("", SearchBar(placeholder = "Search..."))
                path("/users/{userId}", PathIdVisualizer("User", "userId"))
                path("/lists/{listId}", PathIdVisualizer("List", "listId"))
//                path("", MainPage())
                notFound {
                    h1().text("Page not found!")
                }
            }
        }
    }
}

class SearchBar(private val placeholder: String?, private val style: Style = DefaultFomaticStyle) : ComponentTemplate {

    override fun ElementCreator<*>.render() {

        div(style.div) {
            input(type = InputType.search, placeholder = placeholder)
            i(style.icon)
        }
    }

    interface Style {
        val div: Attributes
        val icon: Attributes
    }

    object DefaultFomaticStyle : Style {
        override val div: Attributes get() = fomantic.ui.icon.input
        override val icon: Attributes get() = fomantic.search.icon
    }
}

typealias Attributes = Map<String, JsonPrimitive>

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
typealias Component = (ElementCreator<*>) -> Unit

fun RouteReceiver.path(template: String, component: ComponentTemplate) = path(template, component.asRoutedComponent())

interface ComponentTemplate : Component {

    fun ElementCreator<*>.render()

    override fun invoke(html: ElementCreator<*>) = with(html) { render() }
}

fun ComponentTemplate.asRoutedComponent(): RoutedComponent {

    return object : RoutedComponent {

        override fun invoke(html: ElementCreator<*>, params: Map<String, KVar<String>>) = this@asRoutedComponent.invoke(html)
    }
}

class MainPage : ComponentTemplate {

    override fun ElementCreator<*>.render() {

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