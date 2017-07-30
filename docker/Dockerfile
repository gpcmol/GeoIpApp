FROM openjdk:8-jre-alpine

ENV APP_SLEEP 0
ENV APP_PORT 9090
ENV ARANGO_HOST 0.0.0.0
ENV ARANGO_PORT 8529
ENV ARANGO_USER user
ENV ARANGO_PASSWORD password

# add directly the jar
ADD build/libs/*.jar /app.jar

RUN sh -c 'touch /app.jar'
VOLUME /tmp
EXPOSE 9090
CMD echo "The application will start in ${APP_SLEEP}s..." && \
    sleep ${APP_SLEEP} && \
    java -Djava.security.egd=file:/dev/./urandom -jar /app.jar
