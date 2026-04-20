FROM openjdk:8u121-alpine
LABEL authors="Abdalla Khalifa, Yousef Shafee, Ahmed Basha"

WORKDIR /app

ARG JAR_FILE=food-delivery-app/target/food-delivery-app-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

EXPOSE 8080
