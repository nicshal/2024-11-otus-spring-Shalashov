FROM bellsoft/liberica-openjdk-alpine-musl:21.0.1
WORKDIR /opt/app
COPY /target/hw17-docker.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
