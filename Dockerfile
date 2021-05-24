FROM openjdk:8-jdk-alpine
COPY target/*.jar /auth-svc.jar
ENTRYPOINT ["java","-jar","/auth-svc.jar"]