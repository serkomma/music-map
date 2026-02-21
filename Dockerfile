#---------------------------------- For Kotlin JVM --------------------------------
FROM gradle:jdk25-alpine AS build
WORKDIR /app
COPY . .

# Copy certificate file into the container
COPY download-cdn.jetbrains.com /app/download-cdn.jetbrains.com
# Import the certificate into the Java keystore using keytool
RUN keytool -importcert -file /app/download-cdn.jetbrains.com -alias my-cert-alias -cacerts -storepass changeit -noprompt

RUN gradle :music-map-be:music-map-app-ktor:jvmJar --no-daemon

FROM amazoncorretto:25-alpine as app
WORKDIR /app
COPY --from=build /app/music-map-be/music-map-app-ktor/build/libs/music-map-app-ktor-jvm-0.1.jar application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]

#---------------------------------- For Kotlin Native --------------------------------
FROM ubuntu
COPY ./music-map-be/music-map-app-ktor/build/bin/native/releaseExecutable/music-map-app-ktor.kexe /musicmap
ENTRYPOINT ["/musicmap"]