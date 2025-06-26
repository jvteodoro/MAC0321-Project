#!/bin/bash
docker compose -f database/docker-compose.yaml up -d
cd frontend/app/ 
npm install
npm start
cd ../../
./gradlew bootRun
