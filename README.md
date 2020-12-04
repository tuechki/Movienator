# Movienator #

Project for retrieving information for movies from OMDb database and local dataset.

Here is the [specification](https://gitlab.ontotext.com/SAS/k9/blob/master/architecture.md#functionalities_overview).

## Runtime/deployment properties ##

* sparql.endpoint (REQUIRED, example=http://localhost:7200/) - the URL of the sparql endpoint to which we are sending the request for the data.

* sparql.repository (REQUIRED, example=movieDB) - the name of the repository on the server from which we want to extract data.

* omdb.api (OPTIONAL, default=http://www.omdbapi.com/) - the URL of the Omdb API from where information is extracted if needed.

* connect.timeout (OPTIONAL, default=6000) - the connection timeout for the requests to the Omdb API. (in milliseconds)

* read.timeout (OPTIONAL, default=6000) -  the timeout on waiting to read data using the Omdb API. (in milliseconds)

* omdb.apikey (REQUIRED, example=d8a6b631) - the key for authenticating to the Omdb API. Get from here: http://www.omdbapi.com/apikey.aspx

* graphdb.healthcheck.id (OPTIONAL, default=http://graphdb-status) - The unique ID of the health check.

* graphdb.healthcheck.severity (OPTIONAL, default=HIGH) - The severity level of the health check if it is in ERROR state. Must be one of HIGH, MEDIUM, LOW".

[![Build Status](https://jenkins.ontotext.com/buildStatus/icon?job=movienator-nightly-build)](https://jenkins.ontotext.com/view/movienator/job/movienator-nightly-build/)

## Dataset ##

According to the specification /movies/{movieId}/plot endpoint returns information about the movie including a plot about it.
Since the plots are not present in our dataset, we fetch them from the OMDB API.
Fetched plots are inserted into a separate context(graph) (http://example.org/OMDB/plots) to differentiate them from the actual data.


## Run in a Docker container ##

Run "mvn clean install" in /movienator/api in the console. This will generate an image.
Since the id of the image is different every time, by default the service generate image with name movienator-api:{movienator-api.version}
so, that you would not need the id.
Run "docker images" to see all current images and reassure the desired image is present.

Open a terminal and type "ifconfig". See your local ip and replace localhost in sparql.endpoint with this ip.
Example if your local ip is 192.168.1.188: sparql.endpoint = http://localhost:7200 -> http://192.168.1.188:7200 

Then run:

docker run --env "CATALINA_OPTS= -Dsparql.repository={sparql.repository} -Domdb.apikey={your.apikey} -Dsparql.endpoint={sparql.ednpoint} -p {port} {image.name}

## Run multi-container Docker Application from Movienator API, GraphDB and Elasticsearch ##

1. Make sure that all images used in the docker-compose file are present and updated. If not, download them or
run "mvn clean install" in the desired modules to generate the images with the latest configuration.

2. Configure docker secrets for sensitive data as the OMDB Apikey.
Run "docker swarm init [[OPTIONS]](https://docs.docker.com/engine/reference/commandline/swarm_init/)".
followed by "printf <secret> | docker secret create omdb.apikey -" where <secret> is the actual OMDB Apikey.

3. Run "mvn clean install" in /docker-compose/ directory. This will generate docker-compose.yml file 
in /docker-compose/target/docker with all the needed values from the pom.xml.

4.Navigate to /docker-compose/target/docker directory.
Run "docker stack deploy --compose-file={your_compose_file_in_target_directory} {name_of_your_multi-container_application}"

5. Now your application is running and you can check all active containers using "docker container ls"
You can check here: http://localhost:8888/movienator-api

BONUS: When you are done, remove the all containers using the command:
       "docker stack rm {name_of_your_multi-container_application}"
