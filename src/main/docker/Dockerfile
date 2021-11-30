# Start with a base image containing Java runtime
FROM adoptopenjdk/openjdk11:alpine

# Add Maintainer Info
LABEL maintainer="oktaykocer@outlook.com"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8081

# The application's jar file
ARG JAR_FILE=target/bom-app-be-0.0.1-SNAPSHOT.jar

# The application's properties file
ARG PROPERTIES_FILE=target/classes/docker/application.properties

# Add the application's jar to the container
ADD ${JAR_FILE} bom-app-be.jar

# Add the application's properties to the container
ADD ${PROPERTIES_FILE} application.properties

# Run the jar file
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar bom-app-be.jar -Dspring.config.location=application.properties