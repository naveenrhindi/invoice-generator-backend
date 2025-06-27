# ---------- Step 1: Build Stage ----------
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy the Maven descriptor and download dependencies first (cache-friendly)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the project and generate the jar
RUN mvn clean package -DskipTests

# ---------- Step 2: Run Stage ----------
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
