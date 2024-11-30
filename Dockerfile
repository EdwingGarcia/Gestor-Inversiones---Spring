FROM maven:3.8.6-openjdk-21-slim AS builder
COPY target/Inversiones-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ['java','-jar','/app.jar']