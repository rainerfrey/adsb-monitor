FROM arm32v7/openjdk:11-slim

COPY release/ui-backend.jar /data/ui-backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx64m", "-jar", "/data/ui-backend.jar"] 