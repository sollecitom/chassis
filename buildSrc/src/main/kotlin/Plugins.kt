import org.gradle.api.JavaVersion
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion

object Plugins {

    object JavaPlugin {

        fun configure(plugin: JavaPluginExtension) {
            with(plugin) {
                sourceCompatibility = JavaVersion.VERSION_19
                targetCompatibility = JavaVersion.VERSION_19
                toolchain {
                    languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_19.majorVersion))
                }
                withJavadocJar()
                withSourcesJar()
            }
        }
    }
}