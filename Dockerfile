FROM openjdk:21-jdk-slim
WORKDIR /app
COPY build/libs/document-editor-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
