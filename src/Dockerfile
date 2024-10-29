# Base image sifatida OpenJDK ni tanlash
FROM openjdk:17-jdk-alpine

# Projectni Docker container ichiga copy qilish
COPY target/carea-0.0.1-SNAPSHOT.jar app.jar

# Database yo'llari va sozlamalarni ENV o'zgaruvchilar orqali sozlash
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://dpg-csge5ge8ii6s739gjkhg-a:5432/carea
ENV SPRING_DATASOURCE_USERNAME=posrgres
ENV SPRING_DATASOURCE_PASSWORD=w6PIfb119Qsrt15xFEe0IDlHGNefE9BC

# Application’ni run qilish
ENTRYPOINT ["java", "-jar", "/app.jar"]

