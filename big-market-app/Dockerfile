# ============================================================
# Stage 1: Build (Maven + JDK 21)
# ============================================================
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Resolve dependencies first (cached layer)
COPY pom.xml ./
COPY big-market-api/pom.xml      ./big-market-api/
COPY big-market-app/pom.xml      ./big-market-app/
COPY big-market-domain/pom.xml   ./big-market-domain/
COPY big-market-infrastructure/pom.xml ./big-market-infrastructure/
COPY big-market-trigger/pom.xml  ./big-market-trigger/
COPY big-market-types/pom.xml    ./big-market-types/
RUN mvn dependency:go-offline -q -B

# Copy source and build
COPY . .
RUN mvn package -DskipTests -q -B

# Extract for layered jar
RUN java -Djarmode=layertools -jar big-market-app/target/big-market-app.jar extract --destination /extracted

# ============================================================
# Stage 2: Runtime (JRE 21 only)
# ============================================================
FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S app && adduser -S app -G app && apk add --no-cache curl

ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app

COPY --from=builder /extracted/dependencies/ ./
COPY --from=builder /extracted/spring-boot-loader/ ./
COPY --from=builder /extracted/snapshot-dependencies/ ./
COPY --from=builder /extracted/application/ ./

RUN chown -R app:app /app
USER app

EXPOSE 8091

HEALTHCHECK --interval=30s --timeout=5s --retries=3 --start-period=40s \
    CMD curl -sf http://localhost:8091/actuator/health || exit 1

ENTRYPOINT ["java", \
    "-XX:+UseG1GC", \
    "-XX:MaxGCPauseMillis=200", \
    "-XX:+HeapDumpOnOutOfMemoryError", \
    "-XX:HeapDumpPath=/tmp", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "org.springframework.boot.loader.launch.JarLauncher"]
