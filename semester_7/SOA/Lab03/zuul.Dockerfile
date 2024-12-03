FROM openjdk:17-oracle

COPY zuul/build build

CMD ["java", "-jar", "build/libs/zuul-1.0.jar"]