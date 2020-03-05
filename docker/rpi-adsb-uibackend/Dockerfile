FROM adoptopenjdk:13-jdk-hotspot

COPY ui-backend.jar /data/ui-backend.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx64m", "-jar", "/data/ui-backend.jar"] 