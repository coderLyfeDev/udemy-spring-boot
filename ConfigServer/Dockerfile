#Which docker base image to use
FROM openjdk:8
#what jar file you want to use for the application
ARG JAR_FILE=target/*.jar
#copying that jar file to a new name
COPY ${JAR_FILE} configserver.jar
#start the application when the image builds
ENTRYPOINT ["java", "-jar", "/configserver.jar"]
#what port it runs on.
EXPOSE 9296