#Which docker base image to use
FROM openjdk:8
#what jar file you want to use for the application
ARG JAR_FILE=target/*.jar
#copying that jar file to a new name
COPY ${JAR_FILE} serviceregistry.jar
#start the application when the image builds
ENTRYPOINT ["java", "-jar", "/serviceregistry.jar"]
#what port it runs on.
EXPOSE 8761