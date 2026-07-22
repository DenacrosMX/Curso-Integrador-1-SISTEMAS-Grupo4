FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

FROM tomcat:10.1-jdk21-temurin
RUN rm -rf /usr/local/tomcat/webapps/*

ADD https://repo1.maven.org/maven2/io/prometheus/jmx/jmx_prometheus_javaagent/1.0.1/jmx_prometheus_javaagent-1.0.1.jar /opt/jmx-exporter/jmx_prometheus_javaagent.jar
COPY metricas/jmx-exporter/config.yml /opt/jmx-exporter/config.yml

ENV CATALINA_OPTS="-javaagent:/opt/jmx-exporter/jmx_prometheus_javaagent.jar=9404:/opt/jmx-exporter/config.yml"
ENV JAVA_OPTS="-Ddb.host=postgres -Ddb.port=5432 -Ddb.name=habitech_db -Ddb.user=postgres -Ddb.password=123456"

COPY --from=build /app/target/habitech.war /usr/local/tomcat/webapps/habitech.war

EXPOSE 8080 9404
CMD ["catalina.sh", "run"]
