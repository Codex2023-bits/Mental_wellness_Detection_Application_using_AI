# Step 1: Build the application using Maven
FROM maven:3.9-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package

# Step 2: Use Tomcat to run the application
FROM tomcat:10.1-jdk17-temurin
# Remove default Tomcat apps to keep it clean
RUN rm -rf /usr/local/tomcat/webapps/*
# Copy the built WAR file from the build stage to Tomcat's webapps folder
COPY --from=build /target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]