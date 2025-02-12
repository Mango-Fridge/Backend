# 빌드 단계
FROM gradle:8.12.1-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build --no-daemon --scan

# 실행 단계
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]