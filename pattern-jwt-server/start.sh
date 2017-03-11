#!/bin/bash

mvn install -DskipTests=true

# TODO: Remove hard coded profile.
# nohup mvn spring-boot:run -Dspring.profiles.active=ci >/dev/null 2>&1 &
nohup mvn spring-boot:run -Dspring.profiles.active= >/dev/null 2>&1 &
