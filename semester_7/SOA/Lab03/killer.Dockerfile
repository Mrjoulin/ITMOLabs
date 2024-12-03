# FROM gradle:8.10.2-jdk17 AS build

# WORKDIR /app

# COPY . .
# RUN gradle war

#FROM tomcat:10.1.31 AS RUN
#
#WORKDIR $CATALINA_HOME
#
#COPY ssl-conf/killer-keystore.jks conf/keystore.jks
#COPY ssl-conf/* conf/
#
#RUN rm -rf  webapps/*
#COPY killer/build/libs/killer-1.0-plain.war webapps/ROOT.war
#
#CMD ["bin/catalina.sh", "run"]

#FROM jetty:9.4-jdk17-alpine-amazoncorretto AS RUN
#
#COPY ssl-conf/killer-keystore.jks conf/keystore.jks
#COPY ssl-conf/* conf/
#COPY killer/build/libs/killer-1.0.war $JETTY_BASE/webapps/
#
#ENV JAVA_OPTIONS="-Xmx1g"
#
#CMD ["java", "-jar", "/usr/local/jetty/start.jar"]

FROM openjdk:17-oracle

COPY killer/build build

CMD ["java", "-jar", "build/libs/killer-1.0.jar"]