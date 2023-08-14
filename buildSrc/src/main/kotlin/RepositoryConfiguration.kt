import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import java.net.URI

object RepositoryConfiguration {

    object BuildScript {

        fun apply(config: RepositoryHandler) {
            config.mavenCentral()
        }
    }

    object Modules {

        private const val internalGroup = "${ProjectSettings.rootGroupId}.*"

        fun apply(config: RepositoryHandler, project: Project) {

            config.mavenCentral {
                content {
                    excludeGroupByRegex(internalGroup)
                }
            }

            config.mavenLocal {
                content {
                    includeGroupByRegex(internalGroup)
                }
            }

            config.maven {
                url = URI.create("https://packages.confluent.io/maven")
                content {
                    excludeGroupByRegex(internalGroup)
                }
            }

            config.maven {
                url = URI.create("https://maven.vaadin.com/vaadin-addons")
                content {
                    excludeGroupByRegex(internalGroup)
                }
            }

            config.maven {
                url = URI.create("https://maven.pkg.github.com/sollecitom/*")
                credentials {
                    username = project.findProperty("sollecitom.github.user") as String?
                        ?: System.getenv("GITHUB_USERNAME")
                    password = project.findProperty("sollecitom.github.token") as String?
                        ?: System.getenv("GITHUB_TOKEN")
                }
                content {
                    includeGroupByRegex(internalGroup)
                }
            }
        }
    }
}
