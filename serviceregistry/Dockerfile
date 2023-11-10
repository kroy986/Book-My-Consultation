FROM openjdk:17-jdk-alpine
MAINTAINER upgrad
ADD ./target/service-registry.jar /opt/app/service-registry.jar
WORKDIR /opt/app
ENV PATH="${PATH}:${JAVA_HOME}/bin"
EXPOSE 8761
ENTRYPOINT [ "java", "-jar", "/opt/app/service-registry.jar"]