FROM gradle:8.6.0-jdk21 AS build

WORKDIR /app

COPY . .
RUN gradle bootWar

FROM quay.io/wildfly/wildfly:27.0.0.Final-jdk17

RUN /opt/jboss/wildfly/bin/add-user.sh admin admin --silent
COPY --from=build /app/build/libs/business1-0.0.2-rolling.war /opt/jboss/wildfly/standalone/deployments/