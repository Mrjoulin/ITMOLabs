FROM openjdk:17-oracle

COPY configuration/build build

CMD ["java", "-jar", "build/libs/configuration-1.0.jar"]