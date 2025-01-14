# 贝尔实验室 Spring 官方推荐镜像 JDK下载地址 https://bell-sw.com/pages/downloads/
#FROM bellsoft/liberica-openjdk-debian:17.0.11-cds
#FROM bellsoft/liberica-openjdk-debian:21.0.3-cds
#FROM findepi/graalvm:java17-native
FROM openjdk:21.0.1

LABEL maintainer="zhang san"

RUN mkdir -p /vk/dev/logs

WORKDIR /vk/gen

ENV SERVER_PORT=19006 LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_OPTS=""

ARG JAR_FILE

EXPOSE ${SERVER_PORT}

COPY ./target/${JAR_FILE} ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=${SERVER_PORT}", "${JAVA_OPTS}", "-jar", "app.jar"]