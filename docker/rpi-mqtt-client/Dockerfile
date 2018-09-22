FROM rfy/rpi-build-env

RUN apt-get update && apt-get install -y --no-install-recommends libssl-dev

RUN git clone https://github.com/eclipse/paho.mqtt.c.git \
        && cd paho.mqtt.c \
        && make \
        && make install