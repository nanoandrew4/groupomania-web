FROM mongo:latest
VOLUME /etc/localtime:/etc/localtime:ro

COPY mongodb-keyfile /tmp
RUN chown mongodb:mongodb /tmp/mongodb-keyfile
RUN chmod 600 /tmp/mongodb-keyfile

EXPOSE 27017