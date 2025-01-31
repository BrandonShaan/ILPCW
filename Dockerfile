FROM --platform=linux/amd64 openjdk:21

LABEL authors="brandonshaan"

EXPOSE 8080

WORKDIR /app

COPY ./target/cw1-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar" , "app.jar"]