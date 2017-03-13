#!/bin/bash

if [ "$MYVAR" = "ci" ]
then
   echo "Running CI Environment"
   mvn install -DskipTests=true
fi

nohup mvn spring-boot:run -Dspring.profiles.active=$SPRING_PROFILE >/dev/null 2>&1 &
