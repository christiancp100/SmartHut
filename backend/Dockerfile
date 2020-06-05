FROM openjdk:13-jdk-alpine

ARG JAR_FILE=build/libs/smarthut*.jar

COPY ${JAR_FILE} app.jar

ENV SMARTHUT_THIS_VALUE_IS_PROD_IF_THIS_IS_A_CONTAINER_PIZZOCCHERI=prod
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
