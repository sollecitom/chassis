# Chassis

A set of modular libraries that can be used by multiple projects.

## How to

### Build the project

```bash
./gradlew build

```

### Build the whole projects, including all submodule specific tasks e.g. container-based service tests

#### With cache

```bash
./gradlew build containerBasedServiceTest
```

#### Without cache

```bash
./gradlew clean build containerBasedServiceTest --rerun-tasks
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

### Build all the Docker images (for the example projects)

```bash
./gradlew jibDockerBuild

```