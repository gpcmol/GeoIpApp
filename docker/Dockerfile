FROM openjdk:8-jre-alpine

ENV APP_SLEEP 0
ENV APPLICATION_PORT 9090

# add directly the jar
ADD build/libs/*.jar /app.jar

RUN sh -c 'touch /app.jar'
VOLUME /tmp
EXPOSE 9090
CMD echo "The application will start in ${APP_SLEEP}s..." && \
    sleep ${APP_SLEEP} && \
    java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
