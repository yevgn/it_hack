# Используем официальный образ Maven с JDK 17
FROM maven:3.8.5-openjdk-17-slim AS build

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы проекта в Docker контейнер
COPY . .

# Сборка приложения с Maven
RUN mvn clean package -DskipTests

# Переходим на официальный минимальный образ JDK для исполнения
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранное приложение из предыдущего контейнера
COPY --from=build /app/target/*.jar /app/backend.jar

# Команда для запуска JAR файла
CMD ["java", "-jar", "/app/backend.jar"]