private val optIns = listOf("kotlin.Experimental", "kotlinx.coroutines.ExperimentalCoroutinesApi")
private val optInCompilerArguments = optIns.map { "-opt-in=$it" }
private val compilerArgs = optInCompilerArguments + listOf("-Xcontext-receivers")

object KotlinConfiguration {

    object TargetJvm {

        const val version = "19"
    }

    object Compiler {

        val arguments = compilerArgs
        const val generateJavaParameters = true
    }
}