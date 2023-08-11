package org.sollecitom.chassis.example.webapp.kweb.starter

import kotlinx.coroutines.coroutineScope
import kweb.*
import kweb.plugins.fomanticUI.fomanticUIPlugin
import kweb.state.KVar
import org.http4k.cloudnative.env.Environment
import org.sollecitom.chassis.configuration.utils.formatted
import org.sollecitom.chassis.configuration.utils.fromYamlResource
import org.sollecitom.chassis.example.webapp.kweb.starter.component.template.ComponentTemplate
import org.sollecitom.chassis.example.webapp.kweb.starter.component.template.RoutedComponentTemplate
import org.sollecitom.chassis.example.webapp.kweb.starter.components.Components
import org.sollecitom.chassis.example.webapp.kweb.starter.components.search.bar.searchBar
import org.sollecitom.chassis.example.webapp.kweb.starter.core.extensions.path
import org.sollecitom.chassis.logger.core.loggable.Loggable

suspend fun main() = coroutineScope<Unit> {

    val environment = rawConfiguration().also(::configureLogging)
    logger.info { environment.formatted() }

    Kweb(port = 9000, plugins = listOf(fomanticUIPlugin)) {
        with(Components) {
            val searchBar = searchBar(placeholder = "Search...") {
                onValueChanged { oldValue, newValue ->
                    logger.info { "Searchbar - value changed from ${oldValue.takeIf { it.isNotBlank() } ?: "<EMPTY-${oldValue.length}>"} to ${newValue.takeIf { it.isNotBlank() } ?: "<EMPTY-${newValue.length}>"}" }
                }
                onSearch {
                    logger.info { "Searchbar - search requested with value $value" }
                }
                raw {
                    on.keyup {
                        logger.info { "Searchbar - key pressed ${it.key} and value is now ${element.value.value}" }
                    }
                }
            }
            doc.body {
                route {
                    path("", searchBar)
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
}

class PathIdVisualizer(private val idType: String, private val listIdParamName: String) : RoutedComponentTemplate {

    override fun ElementCreator<*>.render(params: Map<String, KVar<String>>) {

        val listId = params.getValue(listIdParamName)
        h1().text(listId.map { "$idType id: $it" })
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