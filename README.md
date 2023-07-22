# Chassis

A set of modular libraries that can be used by multiple projects.

## How to

### Build the project

```bash
./gradlew build

```

### Publish the artefacts to Maven local

```bash
./gradlew build publishToMavenLocal

```

### Upgrade Gradle (example version)

```bash
./gradlew wrapper --gradle-version 8.2.1 --distribution-type all

```

### Update all dependencies if latest versions exist, and remove unused ones (it will update `gradle/libs.versions.toml`)

```bash
./gradlew versionCatalogUpdate

```