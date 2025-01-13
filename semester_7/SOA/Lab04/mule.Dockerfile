FROM openjdk:8

RUN cd /opt \
&& wget https://s3.amazonaws.com/new-mule-artifacts/mule-ee-distribution-standalone-4.4.0.zip \
&& unzip *.zip \
&& ln -s /opt/mule-enterprise-standalone-4.4.0 /opt/mule \
&& rm /opt/mule-ee-distribution-standalone-4.4.0.zip

ENV MULE_HOME /opt/mule

# Define working directory.
WORKDIR /opt/mule

COPY mule/lib/mulelab04-1.0.0-SNAPSHOT-mule-application.jar /opt/mule/apps/

CMD ["/opt/mule/bin/mule"]