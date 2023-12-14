FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
WORKDIR /app

COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
#docker build --build-arg JAR_FILE=target/*.jar -t genai-server:latest .