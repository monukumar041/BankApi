FROM openjdk:19-alpine
WORKDIR /app
COPY target/bankApi-0.0.1-SNAPSHOT.jar /app/bankApi-0.0.1-SNAPSHOT.jar
CMD ["java","-jar","bankApi-0.0.1-SNAPSHOT.jar"]