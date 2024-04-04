FROM openjdk:17
EXPOSE 8080
ADD target/pfe-0.0.1-SNAPSHOT.jar  pfe-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/pfe-0.0.1-SNAPSHOT.jar"]
