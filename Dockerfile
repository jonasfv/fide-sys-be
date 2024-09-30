# Usa una imagen base con Java 17 y Maven para construir tu aplicación
FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml . 
COPY src ./src
RUN mvn clean package -DskipTests

# Verifica el contenido del directorio de destino para asegurar que el JAR se ha generado
RUN ls -l /app/target/

# Usa una imagen base de JDK para ejecutar tu aplicación
FROM openjdk:17-slim AS runtime
WORKDIR /app

# Copia el archivo JAR generado desde la etapa de construcción
COPY --from=build /app/target/fide-sys-be-0.0.1-SNAPSHOT.jar ./app.jar

# Expone el puerto 8080
EXPOSE 8080

# Comando para ejecutar el JAR
CMD ["java", "-jar", "app.jar"]