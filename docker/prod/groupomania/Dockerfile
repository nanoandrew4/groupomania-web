FROM openjdk:8-jdk-alpine

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar

RUN addgroup -S groupomania-admin && adduser -S groupomania-admin -G groupomania-admin

USER groupomania-admin
ENTRYPOINT ["java","-jar","/app.jar"]

HEALTHCHECK --interval=1m --timeout=10s --retries=3 CMD curl --fail https://localhost:8443/ || exit 1