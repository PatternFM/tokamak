#!/bin/bash

mvn install -DskipTests=true
mvn spring-boot:run -Dspring.profiles.active=ci
#nohup mvn spring-boot:run >/dev/null 2>&1 &
