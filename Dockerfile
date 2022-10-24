# Stage 1 (to create a "build" image, ~140MB)
FROM adoptopenjdk/openjdk11:latest AS builder
RUN java -version

COPY . /usr/src/myapp/
WORKDIR /usr/src/myapp/
RUN apk --no-cache add maven && mvn --version
RUN mvn install 

# Stage 2 (to create a downsized "container executable", ~87MB)
FROM adoptopenjdk/openjdk11:latest
WORKDIR /root/
COPY --from=builder /usr/src/myapp/target/BookApi-1.jar /root/bookApi.jar
 
EXPOSE 8123
ENTRYPOINT ["java", "-jar", "/root/bookApi.jar"]