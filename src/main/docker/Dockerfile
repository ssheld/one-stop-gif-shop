FROM openjdk:8-jdk-alpine
RUN  apk update && apk upgrade && apk add netcat-openbsd && apk add curl
RUN mkdir -p /usr/local/onestopgifshop
ADD @project.build.finalName@.jar /usr/local/onestopgifshop/
EXPOSE 5000
ADD run.sh run.sh
RUN chmod +x run.sh
CMD ./run.sh