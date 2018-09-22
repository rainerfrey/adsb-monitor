FROM rfy/rpi-base

RUN wget -O - http://repo.mosquitto.org/debian/mosquitto-repo.gpg.key | apt-key add -
RUN wget -P /etc/apt/sources.list.d http://repo.mosquitto.org/debian/mosquitto-jessie.list
RUN apt-get update && apt-get install -y --no-install-recommends mosquitto

ADD mosquitto.conf /etc/mosquitto/

EXPOSE 1883

ENTRYPOINT ["/usr/sbin/mosquitto", "-c", "/etc/mosquitto/mosquitto.conf"]
