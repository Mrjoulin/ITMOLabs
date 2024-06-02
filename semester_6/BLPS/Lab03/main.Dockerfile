FROM gradle:8.6.0-jdk21 AS build

WORKDIR /app

COPY . .
RUN gradle bootJar

FROM openjdk:21 AS RUN

COPY --from=build /app/build/libs/business1-0.0.3-rolling.jar /

ENTRYPOINT java -jar business1-0.0.3-rolling.jar