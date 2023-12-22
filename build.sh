#!/bin/bash
./mvnw install -DskipTests

docker build -t tuanminh009/iperform-server:latest .
docker push tuanminh009/iperform-server:latest
ssh minhvt@45.76.184.210 -i ~/.ssh/iperform <<'ENDSSH'
cd iperform
docker compose pull
docker compose up -d