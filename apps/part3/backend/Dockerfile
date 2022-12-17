# ---- Build ----
FROM maven:3.8-eclipse-temurin-17 as builder

RUN mkdir /project
COPY . /project
WORKDIR /project
RUN mvn clean package


# ---- Release ----
FROM gcr.io/distroless/java17-debian11

USER nonroot:nonroot
COPY --from=builder /project/target//backend.jar /app/backend.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/backend.jar"]

