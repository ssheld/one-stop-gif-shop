#!/bin/sh
echo "********************************************************"
echo "Starting One Stop Gif Shop"
echo "********************************************************"
java -Djava.security.egd=file:/dev/./urandom -jar /usr/local/onestopgifshop/@project.build.finalName@.jar
