# Build Application
FROM openjdk:20-bullseye AS build

WORKDIR /app

COPY . .

RUN chmod 755 mvnw

RUN ./mvnw package -DskipTests

# Serve Application
FROM openjdk:20-bullseye

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 6060

CMD [ "java", "--enable-preview", "-jar", "app.jar" ]