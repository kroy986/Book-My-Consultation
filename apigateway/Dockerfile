FROM openjdk:17-jdk-alpine
MAINTAINER upgrad
ADD ./target/api-gateway-service.jar /opt/app/api-gateway-service.jar
WORKDIR /opt/app
ENV PATH="${PATH}:${JAVA_HOME}/bin"
EXPOSE 9756
ENTRYPOINT [ "java", "-jar", "/opt/app/api-gateway-service.jar"]