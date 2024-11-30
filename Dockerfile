# Usa una imagen base con Maven y OpenJDK 11
FROM maven:3.8.6-openjdk-11-slim AS builder

# Establece el directorio de trabajo
WORKDIR /Inversiones

# Copia los archivos del proyecto al contenedor
COPY . /Inversiones

# Ejecuta el build con Maven
RUN mvn clean package -DskipTests

# Usa una imagen más ligera para correr la aplicación
FROM openjdk:11-jre-slim

# Copia el archivo JAR generado desde la imagen builder
COPY --from=builder /Inversiones/target/Inversiones-0.0.1-SNAPSHOT.jar /app.jar

# Define el comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "/app.jar"]
