FROM gradle:8.6.0-jdk21 AS build

WORKDIR /app

COPY . .
RUN gradle bootJar

FROM openjdk:21

COPY --from=build /app/build/libs/business1-0.0.1-rolling.jar /

CMD java -jar business1-0.0.1-rolling.jar