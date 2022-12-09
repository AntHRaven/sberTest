FROM openjdk:17
ARG jarFile=target/sberTestTask-0.0.1-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${jarFile} sberTestTask.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "sberTestTask.jar"]
