# Example service - write endpoint

An example write endpoint service. The test strategy follows the approach mentioned on [this blog](https://sollecitom.github.io/software-product-development-blog/posts/2023/2023-09-07-stop-writing-unit-tests/).

## How to

### Run the container-based system tests

```bash
./gradlew build :chassis-example-write-endpoint-service-test-container-based:containerBasedServiceTest
 

```

### Build the Docker image

```bash
./gradlew build :chassis-example-write-endpoint-service-starter:jibDockerBuild
 

```

### Run and build all

```bash
./gradlew build check
```

### Run the Docker container

#### Without a requested platform

```bash

docker run -p8081:8081 -p8082:8082  ghcr.io/sollecitom-chassis/example-write-endpoint:latest
```

#### Requesting Linux/AMD-64 as a platform

```bash

docker run --platform linux/amd64 -p8081:8081 -p8082:8082  ghcr.io/sollecitom-chassis/example-write-endpoint:latest
```