# FROM gradle:8.10.2-jdk17 AS build

# WORKDIR /app

# COPY . .
# RUN gradle war

FROM tomcat:10.1.31 AS RUN

WORKDIR $CATALINA_HOME

COPY ssl-conf/dragon-keystore.jks conf/keystore.jks
COPY ssl-conf/* conf/

# COPY --from=build /app/build/libs/Lab02-1.0.war webapps/
COPY build/libs/Lab02-1.0.war webapps/

CMD ["bin/catalina.sh", "run"]