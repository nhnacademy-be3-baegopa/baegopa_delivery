FROM eclipse-temurin:11-jre

VOLUME /key
VOLUME /home/nhn/logs
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} app.jar

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${0} ${@}"]