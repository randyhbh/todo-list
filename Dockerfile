FROM maven:3.9.5-eclipse-temurin-17-alpine as builder

WORKDIR application

COPY pom.xml .
COPY src src

RUN mvn install -DskipTests
RUN java -Djarmode=layertools -jar target/*.jar extract

FROM eclipse-temurin:17-jdk
WORKDIR application

COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]