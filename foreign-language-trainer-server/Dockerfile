FROM openjdk:17-alpine
WORKDIR /app
RUN mkdir /app/config
COPY target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]