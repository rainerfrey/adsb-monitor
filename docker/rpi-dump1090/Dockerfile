FROM rfy/rpi-mqtt-client

RUN apt-get update && apt-get install -y --no-install-recommends librtlsdr-dev libusb-1.0-0-dev

RUN echo 'blacklist dvb_usb_rtl28xxu' > /etc/modprobe.d/raspi-blacklist.conf

RUN git clone https://github.com/rainerfrey/dump1090.git \
        && cd dump1090 \
        && git checkout mqtt \
        && dpkg-buildpackage -b \
        && dpkg -i ../dump1090*.deb

EXPOSE 7070

ENTRYPOINT ["/usr/bin/dump1090-mutability",  "--quiet", "--oversample", "--fix", "--phase-enhance", "--max-range", "300", "--net", "--net-ro-size", "500", "--net-ro-interval", "1", "--net-buffer", "2", "--net-http-port", "7070", "--json-location-accuracy", "1"]
