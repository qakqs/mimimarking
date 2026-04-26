# 基础镜像
FROM docker.1ms.run/eclipse-temurin:21-jdk-alpine

# 作者
MAINTAINER xiaofuge

# 配置
ENV PARAMS=""
ENV JAVA_OPTS="-Xms512m -Xmx512m"

# 时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加应用
ADD target/big-market-app.jar /app.jar

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app.jar $PARAMS"]