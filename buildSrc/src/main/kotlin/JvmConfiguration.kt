object JvmConfiguration {

    val mainArgs: List<String> = listOf()
    val testArgs: List<String> = listOf("-XX:+AllowRedefinitionToAddDeleteMethods", "--enable-preview") // TODO remove this AllowRedefinitionToAddDeleteMethods
}