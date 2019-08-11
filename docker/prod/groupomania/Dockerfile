FROM openjdk:8-jdk-alpine

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

RUN addgroup -S groupomania-admin && adduser -S groupomania-admin -G groupomania-admin

USER groupomania-admin

ENV JAVA_OPTS="-Dspring.datasource.url=jdbc:postgresql://postgres:5432/groupomania?currentSchema=groupomania_schema -Dspring.rabbitmq.host=rabbitmq"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app.jar"]

HEALTHCHECK --interval=1m --timeout=10s --retries=3 CMD curl --fail https://127.0.0.1:8443/ || exit 1