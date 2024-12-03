# FROM gradle:8.10.2-jdk17 AS build

# WORKDIR /app

# COPY . .
# RUN gradle war

#FROM tomcat:10.1.31 AS RUN
#
#WORKDIR $CATALINA_HOME
#
#COPY ssl-conf/dragon-keystore.jks conf/keystore.jks
#COPY ssl-conf/* conf/
#
## COPY --from=build /app/build/libs/Lab02-1.0.war webapps/
#RUN rm -rf  webapps/*
#COPY build/libs/Lab03-1.0-plain.war webapps/ROOT.war
#
#CMD ["bin/catalina.sh", "run"]

FROM openjdk:17-oracle

COPY build build

CMD ["java", "-jar", "build/libs/Lab03-1.0.jar"]