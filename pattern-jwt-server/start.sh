#!/bin/bash

cd /Users/brandongrenier/Projects/pattern/pattern-jwt/pattern-jwt-server 

nohup mvn spring-boot:run >/dev/null 2>&1 &
