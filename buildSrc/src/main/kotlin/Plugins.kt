import org.gradle.api.JavaVersion
import org.gradle.api.plugins.JavaPluginExtension

object Plugins {

    object JavaPlugin {

        fun configure(plugin: JavaPluginExtension) {
            with(plugin) {
                sourceCompatibility = JavaVersion.VERSION_19
                targetCompatibility = JavaVersion.VERSION_19
                withJavadocJar()
                withSourcesJar()
            }
        }
    }
}