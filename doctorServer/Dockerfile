FROM openjdk:17-jdk-alpine
MAINTAINER upgrad
ADD ./target/doctor-service.jar /opt/app/doctor-service.jar
WORKDIR /opt/app
ENV PATH="${PATH}:${JAVA_HOME}/bin"
EXPOSE 9005
ENTRYPOINT [ "java", "-jar", "/opt/app/doctor-service.jar"]