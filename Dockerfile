FROM eclipse-temurin:17-jre

ADD target/app.jar app.jar
ADD target/app.jar.original app.jar.original
ADD target/agent/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

ENTRYPOINT java -javaagent:opentelemetry-javaagent.jar -jar app.jar