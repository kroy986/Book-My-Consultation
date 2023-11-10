FROM openjdk:17-jdk-alpine
MAINTAINER upgrad
ADD ./target/authentication-service.jar /opt/app/authentication-service.jar
WORKDIR /opt/app
ENV PATH="${PATH}:${JAVA_HOME}/bin"
EXPOSE 8989
ENTRYPOINT [ "java", "-jar", "/opt/app/authentication-service.jar"]