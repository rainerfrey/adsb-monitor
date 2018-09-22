FROM rfy/rpi-base

RUN adduser --system --uid 110 --home /var/lib/mongodb --no-create-home mongodb && mkdir -p /var/lib/mongodb && chown mongodb /var/lib/mongodb
COPY mongod /usr/bin/

USER mongodb

ENTRYPOINT ["/usr/bin/mongod", "--nounixsocket", "--smallfiles", "--dbpath", "/var/lib/mongodb"]