FROM openjdk:17
EXPOSE 8080
COPY target/workhub-0.0.1-SNAPSHOT.jar workhub.jar
ENTRYPOINT ["java","-jar","/workhub.jar"]