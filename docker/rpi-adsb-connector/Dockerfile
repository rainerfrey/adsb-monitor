FROM adoptopenjdk:13-jdk-hotspot

COPY dump1090-connector.jar /data/dump1090-connector.jar

EXPOSE 9090

ENTRYPOINT ["java", "-Xmx64m", "-jar", "/data/dump1090-connector.jar"]