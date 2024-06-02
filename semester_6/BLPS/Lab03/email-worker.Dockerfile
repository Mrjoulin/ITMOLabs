FROM gradle:8.6.0-jdk21 AS build

WORKDIR /app

COPY . .
RUN gradle bootJar

FROM openjdk:21 AS RUN

COPY --from=build /app/email-worker/build/libs/email-worker-0.0.3-rolling.jar /

ENTRYPOINT java -jar email-worker-0.0.3-rolling.jar