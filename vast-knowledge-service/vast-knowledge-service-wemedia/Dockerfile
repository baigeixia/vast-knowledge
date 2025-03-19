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
ENV SERVER_PORT=19005 \
    LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    JAVA_OPTS="" \
    NACOS_NAMESPACE=549a7c16-6c77-48a3-8925-5a880f59da6f \
    NACOS_HOST=nc-svc.nc.svc.cluster.local \
    NACOS_PORT=8848 \
    ACTIVE=prod
# 暴露端口
EXPOSE ${SERVER_PORT}

# 复制 JAR 文件
COPY ./target/*.jar ./app.jar

# 使用 Shell 形式的 ENTRYPOINT，以便解析环境变量
ENTRYPOINT exec java -Djava.security.egd=file:/dev/./urandom \
                                 -Dnacos.namespace=$NACOS_NAMESPACE \
                                 -Dnacos.discovery.server-addr=$NACOS_HOST:$NACOS_PORT \
                                 -Dspring.profiles.active=$ACTIVE \
                                 $JAVA_OPTS -jar app.jar