# Example service - write endpoint

An example write endpoint service.

## How to

### Build the Docker image

```bash
./gradlew build chassis-example-service-write-endpoint-starter:jibDockerBuild
 

```

### Run the Docker container

```bash

docker run -p8081:8081 -p8082:8082  ghcr.io/sollecitom-chassis/example-write-endpoint:latest
```