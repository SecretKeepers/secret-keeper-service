FROM eclipse-temurin:17-alpine

WORKDIR /app

COPY target/service-1.0.jar /app/

EXPOSE 8080

CMD ["java", "-jar", "service-1.0.jar"]