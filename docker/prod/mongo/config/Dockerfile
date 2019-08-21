FROM mongo:latest
VOLUME /etc/localtime:/etc/localtime:ro

ENV MONGO_INITDB_ROOT_USERNAME greenapperadmin
ENV MONGO_INITDB_ROOT_PASSWORD supersecretpassword
ENV MONGO_INITDB_DATABASE loggingdb

COPY mongodb-keyfile /tmp
RUN chown mongodb:mongodb /tmp/mongodb-keyfile
RUN chmod 600 /tmp/mongodb-keyfile

EXPOSE 27017