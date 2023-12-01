FROM eclipse-temurin:21-jdk-alpine as builder

WORKDIR application

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN java -Djarmode=layertools -jar target/*.jar extract

FROM eclipse-temurin:21-jdk-alpine
WORKDIR application

COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]