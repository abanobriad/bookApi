FROM adoptopenjdk/openjdk11:alpine-jre
ARG JAR_FILE=target/BookApi-1.jar
COPY ${JAR_FILE} bookApp.jar
ENTRYPOINT ["java","-jar","bookApp.jar"]