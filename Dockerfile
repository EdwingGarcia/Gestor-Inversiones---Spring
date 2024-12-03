# Usamos una imagen base con Java 17
FROM openjdk:17-jdk-slim as build

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo pom.xml y los archivos fuente al contenedor
COPY pom.xml .

# Descargar las dependencias necesarias para el build (sin compilar aún)
RUN mvn dependency:go-offline

# Copiar el código fuente
COPY src ./src

# Compilar el proyecto usando Maven
RUN mvn clean package -DskipTests

# Fase de ejecución (runtime)
FROM openjdk:17-jdk-slim

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiar el archivo .jar compilado desde la fase anterior
COPY --from=build /app/target/Inversiones-0.0.1-SNAPSHOT.jar /app/Inversiones.jar

# Exponer el puerto 8080 (comúnmente utilizado por Spring Boot)
EXPOSE 8080

# Ejecutar la aplicación Spring Boot
ENTRYPOINT ["java", "-jar", "Inversiones.jar"]
