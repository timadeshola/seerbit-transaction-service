#Build environment
FROM gradle:alpine AS GRADLE_BUILD
ENV APP_HOME=/app/workspace
WORKDIR $APP_HOME

COPY build.gradle settings.gradle gradlew $APP_HOME
COPY gradle $APP_HOME/gradle

COPY . $APP_HOME
RUN ./gradlew clean build

FROM eclipse-temurin:17-jdk-alpine
MAINTAINER John Adeshola cyberstarsinfo@gmail.com
ENV ARTIFACT_NAME=transaction-service-0.0.1.jar
ARG DEPENDENCY=/app/workspace
ENV APP=/app
WORKDIR $APP
COPY --from=GRADLE_BUILD $DEPENDENCY/build/libs/$ARTIFACT_NAME $APP/$ARTIFACT_NAME
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "transaction-service-0.0.1.jar"]

