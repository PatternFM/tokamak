#!/bin/bash

mvn install -DskipTests=true
nohup mvn spring-boot:run -Dspring.profiles.active=ci >/dev/null 2>&1 &
