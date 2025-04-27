FROM openjdk:19
COPY target/Bierkasse-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources /resources
COPY env.properties env.properties
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]