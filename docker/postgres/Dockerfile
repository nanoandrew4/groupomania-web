FROM postgres:12-alpine

#RUN addgroup -S greenapperdba && adduser -S greenapperdba -G greenapperdba

COPY db-initializer-script.sql /docker-entrypoint-initdb.d/
#USER greenapperdba

HEALTHCHECK --interval=10s --timeout=5s --retries=5 CMD pg_isready -U greenapperdba -d groupomania || exit 1