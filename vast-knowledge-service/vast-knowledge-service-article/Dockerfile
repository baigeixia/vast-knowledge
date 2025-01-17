# 贝尔实验室 Spring 官方推荐镜像 JDK下载地址 https://bell-sw.com/pages/downloads/
# 使用 BellSoft Liberica JDK 镜像
FROM dragonwell-registry.cn-hangzhou.cr.aliyuncs.com/dragonwell/dragonwell:21

# 维护者信息
LABEL maintainer="zhang san"

# 创建日志目录
RUN mkdir -p /vk/dev/logs

# 设置工作目录
WORKDIR /vk/gen

# 设置环境变量  与 application.yml保存一致
ENV SERVER_PORT=19004 LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_OPTS=""

# 暴露端口
EXPOSE ${SERVER_PORT}

# 复制 JAR 文件
COPY ./target/*.jar ./app.jar

# 使用 Shell 形式的 ENTRYPOINT，以便解析环境变量
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom $JAVA_OPTS -jar app.jar