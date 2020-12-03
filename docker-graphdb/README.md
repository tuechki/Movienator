# Docker-GraphDB #

## What is this ##
This module is responsible for the image of GraphDB needed for the successful running of the Movienator API.
It copies all the needed files and create an image ready to be turned out in a container.

## How to generate the image ##
Go under the base directory (/docker-graphdb) and run "mvn clean install". An image with name docker-graphdb
and tag - the version of the project will be generated.