FROM maven:3.8-eclipse-temurin-17 as builder
RUN mkdir /project
COPY . /project
WORKDIR /project
RUN mvn clean package -DskipTests
RUN mkdir -p target/dependency
WORKDIR /project/target/dependency
RUN jar -xf ../*.jar

FROM gcr.io/distroless/java17-debian11
USER nonroot:nonroot
ARG DEPENDENCY=/project/target/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app
EXPOSE 8080
ENTRYPOINT ["java","-cp","app:app/lib/*","hello.Application"]
