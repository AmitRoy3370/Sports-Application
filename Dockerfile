# ---------- Stage 1: Build with Java 25 ----------
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Install Maven manually (no prebuilt maven images for Java 25)
RUN apt-get update && apt-get install -y maven git curl && rm -rf /var/lib/apt/lists/*

# Copy POM and download dependencies offline
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the jar
COPY src ./src
RUN mvn clean package -DskipTests

# ---------- Stage 2: Run ----------
FROM eclipse-temurin:25-jdk-alpine
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Increase file upload handling and memory (JVM options)
ENV JAVA_OPTS="-Xms512m -Xmx2g -Dspring.servlet.multipart.max-file-size=50000MB -Dspring.servlet.multipart.max-request-size=50000MB -Dserver.tomcat.max-swallow-size=50000MB"

# Expose port
EXPOSE 8080

# Start the application with JVM options
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]