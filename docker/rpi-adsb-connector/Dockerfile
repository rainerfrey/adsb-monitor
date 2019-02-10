FROM arm32v7/openjdk:10-slim

COPY release/dump1090-connector.jar /data/dump1090-connector.jar

EXPOSE 9090

ENTRYPOINT ["java", "-Xmx64m", "-jar", "/data/dump1090-connector.jar"]