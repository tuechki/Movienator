#!/bin/bash

/opt/graphdb/dist/bin/graphdb &

echo -e "\nWaiting GraphDB to start..."

for i in $(seq 1 60); do
  CHECK_RES=$(curl --silent --write-out '%{http_code}' --output /dev/null http://localhost:7200/rest/repositories)
    if [ "${CHECK_RES}" = '200' ]; then
      echo -e "\nUp and running"
      break
    fi
  sleep 3
  echo "CHECK_RES: ${CHECK_RES}"
done

echo "Creating a repository....";
curl -X POST --header "Content-Type:multipart/form-data" -F "config=@/root/config.ttl" http://localhost:7200/rest/repositories

echo "Adding statements...."
curl -X POST -H "Content-Type:application/trig" -T /root/statements.trig http://localhost:7200/repositories/movieDB/statements

sed -i "s|ELASTICSEARCH_URI|${ELASTICSEARCH_URI}|g" /root/connector.rq
sed -i "s|ELASTICSEARCH_PORT|${ELASTICSEARCH_PORT}|g" /root/connector.rq

echo "Creating an elasticsearch connector...."
curl -X POST -H "Content-Type:application/sparql-update" -T /root/connector.rq http://localhost:7200/repositories/movieDB/statements

wait