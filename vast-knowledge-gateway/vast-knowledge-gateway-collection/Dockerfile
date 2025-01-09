# 贝尔实验室 Spring 官方推荐镜像 JDK下载地址 https://bell-sw.com/pages/downloads/
# 使用 JDK 作为基础镜像
FROM openjdk:21-slim

# 创建目录并复制文件
RUN mkdir -p /vk/gateway/logs /vk/gateway/temp /vk/skywalking/agent
WORKDIR /vk/gateway

# 设置环境变量
ENV SERVER_PORT=16003 LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_OPTS=""

# 复制 JAR 文件
COPY ./target/vast-knowledge-gateway-collection.jar ./gateway-collection.jar

# 暴露端口
EXPOSE ${SERVER_PORT}

# 启动应用
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=${SERVER_PORT}", \
           "-XX:+HeapDumpOnOutOfMemoryError", "-XX:+UseZGC", "${JAVA_OPTS}", \
           "-jar", "/vk/gateway/gateway-collection.jar"]
