#!/bin/bash

apiKeySecret=$(cat "/run/secrets/omdb.apikey")


export CATALINA_OPTS="${CATALINA_OPTS}  -Dsparql.endpoint=$SPARQL_ENDPOINT
                                        -Dsparql.repository=$SPARQL_REPOSITORY
                                        -Domdb.apikey=$apiKeySecret
                                        -Xdebug -Xrunjdwp:transport=dt_socket,address=28888,server=y,suspend=n
                                        -Dcom.sun.management.jmxremote=true
                                        -Dcom.sun.management.jmxremote.port=9010
                                        -Dcom.sun.management.jmxremote.local.only=false
                                        -Dcom.sun.management.jmxremote.authenticate=false
                                        -Dcom.sun.management.jmxremote.ssl=false
                                        -Djava.rmi.server.hostname=localhost
                                        -Dcom.sun.management.jmxremote.rmi.port=9010"


$CATALINA_HOME/bin/catalina.sh run