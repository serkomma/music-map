FROM gradle:jdk25-alpine AS build
WORKDIR /app
COPY --from=musicappdirectory . .
RUN gradle :music-map-be:music-map-tmp:build --no-daemon

FROM amazoncorretto:25-alpine as app
WORKDIR /app
COPY --from=build /app/music-map-be/music-map-tmp/build/libs/*.jar music-map-tmp.jar
ENTRYPOINT ["java", "-jar", "music-map-tmp.jar"]