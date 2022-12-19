
ARG BUILD_BASE=ghcr.io/graalvm/native-image:ol8-java17-22.1.0
#ARG BUILD_BASE=container-registry.oracle.com/graalvm/native-image-ee:ol8-java17-22.1.0

ARG FDK_RUNTIME=fnproject/fn-java-fdk:jre17-1.0.162
ARG RUNTIME_BASE=container-registry.oracle.com/os/oraclelinux:8-slim

### STEP 1 - generate native image
FROM ${BUILD_BASE} as native-image-build
WORKDIR /function

# build native image
ADD pom.xml pom.xml
ADD mvnw mvnw
ADD .mvn .mvn
RUN ["./mvnw", "package", "-DskipTests=true"]
ADD src src
RUN ["./mvnw", "-Pnative", "package", "-DskipTests=true"]
# note: native image (func) is located under /function/target

### STEP 2 - prepare libfnunixsocket.so from fdk-runtime image
FROM ${FDK_RUNTIME} as fdk-runtime
# note: libfnunixsocket.so is located under /function/runtime/lib

### STEP 3 - build fn runtime image
FROM ${RUNTIME_BASE}
WORKDIR /function

# copy native image
COPY --from=native-image-build /function/target/func .

# copy libfnunixsocket.so
COPY --from=fdk-runtime /function/runtime/lib/libfnunixsocket.so .

# final settings
ENTRYPOINT ["./func", "-XX:MaximumHeapSizePercent=80", "-Djava.library.path=/function"]
CMD [ "com.example.fn.HelloFunction::handleRequest" ]
