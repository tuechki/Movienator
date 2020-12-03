# Troubleshooting the Movienator Service
Synopsis: a document designed to help identify and resolve known issues with the Movienator service.

## Table of Contents

1. [Introduction](#introduction)
	* [Service Overview](#service_overview)
		* [Context Diagram](#context_diagram)
		* [Important endpoints](#important_endpoints)
			* [Movies](#movies_endpoint)
			* [Movie By Id](#movie_by_id_endpoint)
			* [Movie By Id With Plot](#movie_by_id_with_plot_endpoint)
			* [Health](#health)
			* [Good to Go](#gtg)
			* [About](#about)   
			* [Trouble](#trouble)
	* [References](#references) 
2. [Prerequisites](#prerequisites)
3. [Resolving Known Error Conditions](#resolve)
	* [Symptoms](#symptoms)
		* [Service cannot be started](#symptom_1)
		* [Movie GraphDB Health Check returns status WARNING](#symptom_2)
		* [Movie GraphDB Health Check returns status ERROR](#symptom_3)
		* [GraphDB Health Check fails](#symptom_4)
		* [Movie plot endpoint returns status code 500](#symptom_5)
		* [Movie plot endpoint returns status code 502](#symptom_6)
		* [Movie endpoint returns status code 500](#symptom_7)
		* [Movies endpoint returns status code 500](#symptom_8)
	* [Causes](#causes)
	* [Missing arguments](#missing-arguments)
			* [Solution](#solution_missing-arguments)
			* [Verification](#verification_missing-arguments)
		* [Invalid arguments](#invalid-arguments)
			* [Solution](#solution_invalid-arguments)
			* [Verification](#verification_invalid-arguments)
		* [Movie GraphDB Health Check returns status WARNING](#movie-graphdb-health-check-returns-status-warning)
			* [Solution](#solution_movie-graphdb-health-check-returns-status-warning)
			* [Verification](#verification_movie-graphdb-health-check-returns-status-warning)
		* [Movie GraphDB Health Check returns status ERROR](#movie-graphdb-health-check-returns-status-error)
			* [Solution](#solution_movie-graphdb-health-check-returns-status-error)
			* [Verification](#verification_movie-graphdb-health-check-returns-status-error)
		* [GraphDB Health Check fails](#graphdb-health-check-fails)
			* [Solution](#solution_graphdb-health-check-fails)
			* [Verification](#verification_graphdb-health-check-fails)
		* [GraphDB is unreachable](#graphdb-unreachable)
			* [Solution](#solution_graphdb-unreachable)
			* [Verification](#verification_graphdb-unreachable)
		* [GraphDB is down](#graphdb-down)
			* [Solution](#solution_graphdb-down)
			* [Verification](#verification_graphdb-down)
		* [GraphDB cannot connect to Elasticsearch](#graphdb-cannot-connect-elasticsearch)
			* [Solution](#solution_graphdb-cannot-connect-elasticsearch)
			* [Verification](#verification_graphdb-cannot-connect-elasticsearch)
		* [Elasticsearch is down](#elasticsearch-down)
			* [Solution](#solution_elasticsearch-down)
			* [Verification](#verification_elasticsearch-down)
		* [Missing GraphDB repository](#missing-graphdb-repository)
			* [Solution](#solution_missing-graphdb-repository)
			* [Verification](#verification_missing-graphdb-repository)
		* [OMDb API failed](#omdb-api-failed)
			* [Solution](#solution_omdb-api-failed)
			* [Verification](#verification_omdb-api-failed)
		* [Invalid API key](#invalid-apikey)
			* [Solution](#solution_invalid-apikey)
			* [Verification](#verification_invalid-apikey)

<a name="introduction"></a>
## Introduction
This document is designed to help troubleshoot the Movienator service. It [describes](#service_overview) the service, the [context](#context_diagram) in which it resides, and lists its most [important endpoints](#important_endpoints).

The reader of this document should have the [prerequisite](#prerequisites) skills for the document to be effective. [Known issues](#resolve) and solutions are documented.

<a name="service_overview"></a>
## Movienator API Service Overview

|                   |                                                    |
|:----------------- |:---------------------------------------------------|
| Description       | Movienator service is a Spring Boot service exposing a REST API for accessing information about movies.It exposes to the client the data from a movies dataset in RDF enriched with data from a third party API - the OMDb API.The service allows accessing a movie information by ID, retrieving its plot and searching movies based on a similarity of their titles with a given query string and based on actors starring.                            |
| Entry Point URL   | http://{host}:{port}/{movienator-api}      |

<a name="context_diagram"></a>
### Context Diagram

<img src="https://www.lucidchart.com/publicSegments/view/7a843eb5-332d-4757-bbcf-a57218ad1581/image.jpeg"
   style="width:70%;height:85%;"
   alt="No diagram found">

<a name="important_endpoints"></a>
### Important endpoints

<a name="movies_endpoint"></a>
#### Movies Endpoint

Returns a list of movies according to the parameters passed down in the query.

| Verb | URL template   | Mime Type        | Supported Status Codes |
|:-----|:---------------|:-----------------|:----------------------:|
| GET  | /movies        | application/json | 200, 400               |

##### Curl request
```
curl -X GET --header 'Accept: application/json' 'http://{host}:{port}/{movienator-api}/movies'
```

#### Example Response

```json
{  
   "count":5043,
   "movies":[  
      {  
         "id":"6",
         "title":"John Carter",
         "language":"English",
         "released":2012,
         "imdbLink":"http://www.imdb.com/title/tt0401729"
      },
      {  
         "id":"8",
         "title":"Tangled",
         "language":"English",
         "released":2010,
         "imdbLink":"http://www.imdb.com/title/tt0398286"
      }
   ]
}
```


<a name="movie_by_id_endpoint"></a>
#### Movie By Id Endpoint

Returns information about the specified movie in the query.

| Verb | URL template      | Mime Type        | Supported Status Codes |
|:-----|:------------------|:-----------------|:----------------------:|
| GET  | /movies/{movieId} | application/json | 200, 404               |

##### Curl request
```
curl -X GET --header 'Accept: application/json' 'http://{host}:{port}/{movienator-api}/movies/{movieId}'
```

#### Example Response

```json
{  
   "id":"3",
   "title":"Spectre",
   "language":"English",
   "released":2015,
   "budget":245000000,
   "gross":200074175,
   "aspectRatio":2.35,
   "imdbRating":6.8,
   "castTotalFBLikes":11700,
   "movieFBLikes":85000,
   "runtime":148,
   "color":"Color",
   "country":"UK",
   "director":"Sam Mendes",
   "actors":[  
      "Christoph Waltz",
      "Rory Kinnear",
      "Stephanie Sigman"
   ],
   "genres":[  
      "Action",
      "Adventure",
      "Thriller"
   ],
   "numCritics":602,
   "numUserReviews":994,
   "numUserVotes":275868,
   "numFacesInPoster":1,
   "keywords":[  
      "bomb",
      "espionage",
      "sequel",
      "spy",
      "terrorist"
   ],
   "imdbLink":"http://www.imdb.com/title/tt2379713"
}
```

<a name="movie_by_id_with_plot_endpoint"></a>
#### Movie By Id With Plot Endpoint

Returns brief information about the specified movie including plot for it.

| Verb  | URL template           | Mime Type        | Supported Status Codes |
|:------|:-----------------------|:-----------------|:----------------------:|
| PATCH | /movies/{movieId}/plot | application/json | 200, 201, 404, 502     |

##### Curl request
```
curl -X PATCH --header 'Accept: application/json' 'http://{host}:{port}/{movienator-api}/movies/{movieId}/plot'
```

#### Example Response

```json
{
  "id": "3",
  "title": "Spectre",
  "plot": "A cryptic message from 007's past sends him pitted against a mysterious terrorist organization called Spectre, and learns of its involvement in previous events of his most dangerous missions."
}
```

<a name="health"></a>
#### Health endpoint

Returns JSON data summarising the current health status of the service. The resultant JSON must include status for GraphDB and ES availability.
If GraphDB or ES is not accessible the status is ERROR, OK otherwise.

The status code must always be a 200 unless the /\__health endpoint itself is in error.

| Verb | URL template | Mime Type         | Supported Status Codes |
|:-----|:-------------|:------------------|:----------------------:|
| GET  | /\__health   | application/json  | 200                    |

#### Curl request

```
curl -X GET --header 'Accept: application/json' 'http://{host}:{port}/{movienator-api}/__health'
```

#### Example Response

```json
{  
   "status":"OK",
   "healthChecks":[  
      {  
         "status":"OK",
         "severity":"HIGH",
         "id":"graphDbId",
         "name":"GraphDB Health Check",
         "type":"graphdb",
         "impact":"Data for movies won't be retrieved.",
         "troubleshooting":"http://localhost:8080/__trouble",
         "description":"Runs GraphDB health checks, checks GraphDB version and protocol.",
         "protocol":8,
         "version":"8.10.1+sha.a6115dda",
         "healthCheckResponse":{  
            "name":"movieDB",
            "status":"green",
            "components":[  
               {  
                  "name":"read-availability",
                  "status":"green"
               },
               {  
                  "name":"storage-folder",
                  "status":"green"
               },
               {  
                  "name":"long-running-queries",
                  "status":"green"
               },
               {  
                  "name":"predicates-statistics",
                  "status":"green"
               },
               {  
                  "name":"plugins",
                  "status":"green",
                  "components":[  
                     {  
                        "name":"elasticsearch-connector",
                        "status":"green",
                        "components":[  
                           {  
                              "name":"movies",
                              "status":"green",
                              "message":"query took 0 ms, 5043 hits, 0 failed shards"
                           },
                           {  
                              "name":"movies-copy",
                              "status":"green",
                              "message":"query took 0 ms, 5043 hits, 0 failed shards"
                           }
                        ]
                     },
                     {  
                        "name":"lucene-connector",
                        "status":"green",
                        "components":[]
                     },
                     {  
                        "name":"solr-connector",
                        "status":"green",
                        "components":[]
                     }
                  ]
               }
            ]
         }
      }
   ]
}
```

<a name="gtg"></a>
#### Good To Go endpoint

The /\__gtg endpoint should be based on the health check status results. One or more errors imply that the service is not good to go.
The endpoint emits a 200 OK response if the application is considered as healthy, and 503 Service Unavailable if it is unhealthy. 
This endpoint is intended to be used to make routing decisions and provide operations personnel simple access to service availability.

| Verb | URL template | Mime Type        | Supported Status Codes |
|:-----|:-------------|:-----------------|:----------------------:|
| GET  | /\__gtg      | application/json | 200, 503               |

#### Good to go Response

The resultant JSON must include an aggregated status for all out of process service dependencies.
OK only when all health checks are 200.

##### Curl request
```
curl -X GET --header 'Accept: application/json' 'http://{host}:{port}/{movienator-api}/__gtg'
```

#### OK Response

```json
{
  "gtg": "OK"
}
```

#### UNAVAILABLE Response

```json
{
  "gtg": "UNAVAILABLE"
}
```

<a name="about"></a>
#### About endpoint

Returns JSON data describing the service.
It must include the running version.

| Verb | URL template | Mime Type        | Supported Status Codes |
|:-----|:-------------|:-----------------|:----------------------:|
| GET  | /\__about    | application/json | 200                    |

##### Curl request
```
curl -X GET --header 'Accept: application/json' 'http://{host}:{port}/{movienator-api}/__about'

```

#### Example Response

```json
{
  "buildDate": "2019-07-15T10:50:41Z",
  "description": "Movienator service is a Spring Boot service exposing a REST API for accessing information about movies.It exposes to the client the data from a movies dataset in RDF enriched with data from a third party API - the OMDb API.The service allows accessing a movie information by ID, retrieving its plot and searching movies based on a similarity of their titles with a given query string and based on actors starring.",
  "version": "1.0.0"
}
```

<a name="trouble"></a>
#### Trouble endpoint

The /\__trouble endpoint renders the trouble.md document of the service.

| Verb | URL template | Mime Type  | Supported Status Codes |
|:-----|:-------------|:-----------|:----------------------:|
| GET  | /\__trouble  | text/plain | 200                    |

##### Curl request
```
curl -X GET --header 'Accept: text/plain' 'http://{host}:{port}/{movienator-api}/__trouble'
```
<a name="references"></a>
## References
This document should be referenced directly from appropriate erroneous HTTP status code message body content. Thus providing a resolution path to mitigate errors and in some cases restore service.

<a name="prerequisites"></a>
# Prerequisites
Users, developers and testers who wish to effectively troubleshoot the Movienator API service must be at least moderately experienced with the following:

* Experience with Linux
* Experience with bash
* Experience with curl


<a name="resolve"></a>
# Resolving Known Error Conditions

<a name="symptoms"></a>
## Symptoms

<a name="symptom_1"></a>
### Service cannot be started
Possible causes:
- [Missing arguments](#missing-arguments)
- [Invalid arguments](#invalid-arguments)

<a name="symptom_2"></a>
### Movie GraphDB Health Check returns status WARNING
Most likely cause: [Movie GraphDB Health Check returns status WARNING](#movie-graphdb-health-check-returns-status-warning)

<a name="symptom_3"></a>
### Movie GraphDB Health Check returns status ERROR
Possible causes:
- [GraphDB down](#graphdb-down)
- [GraphDB unreachable](#graphdb-unreachable)
- [GraphDB Health Check fails](#graphdb-health-check-fails)


<a name="symptom_4"></a>
### GraphDB Health Check fails
Possible causes:
- [GraphDB cannot connect to Elasticsearch](#graphdb-cannot-connect-elasticsearch)
- [Elasticsearch is down](#elasticsearch-down)
- [Missing GraphDB repository](#missing-graphdb-repository)
- [GraphDB Health Check fails](#graphdb-health-check-fails)

<a name="symptom_5"></a>
### Movie plot endpoint returns status code 500
Possible causes:
- [GraphDB is down](#graphdb-down)
- [GraphDB is unreachable](#graphdb-unreachable)
- [Movie GraphDB Health Check returns status ERROR](#movie-graphdb-health-check-returns-status-error)
- [Invalid API key](#invalid-apikey)      
- [Missing GraphDB repository](#missing-graphdb-repository)

<a name="symptom_6"></a>
### Movie plot endpoint returns status code 502
Most likely cause: [OMDb API failed](#omdb-api-failed)

<a name="symptom_7"></a>
### Movie endpoint returns status code 500
Possible causes:
- [GraphDB is down](#graphdb-down)
- [GraphDB is unreachable](#graphdb-unreachable)
- [Movie GraphDB Health Check returns status ERROR](#movie-graphdb-health-check-returns-status-error)
- [Missing GraphDB repository](#missing-graphdb-repository)

<a name="symptom_8"></a>
### Movies endpoint returns status code 500
Possible causes:
- [GraphDB is down](#graphdb-down)
- [GraphDB is unreachable](#graphdb-unreachable)
- [GraphDB cannot connect to Elasticsearch](#graphdb-cannot-connect-elasticsearch)
- [Movie GraphDB Health Check returns status ERROR](#movie-graphdb-health-check-returns-status-error)
- [Elasticsearch is down](#elasticsearch-down)
- [Missing GraphDB repository](#missing-graphdb-repository)

<a name="causes"></a>
## Causes

<a name="missing-arguments"></a>
### Missing arguments

| #                 | Detail                                                                     |
|:----------------- |:---------------------------------------------------------------------------|
| Cause             | Absence of arguments.                                                      |
| Symptom {1}       | [Service cannot be started](#symptom_1) IllegalArgumentException is thrown.|

<a name="solution_missing-arguments"></a>
#### Solution
Check the README.md to see the REQUIRED arguments and add them.

<a name="verification_missing-arguments"></a>
#### Verification
Request to /\__gtg endpoint of the service returns OK.


<a name="invalid-arguments"></a>
### Invalid arguments

| #                 | Detail                                                                |
|:----------------- |:----------------------------------------------------------------------|
| Cause             | Invalid arguments.                                                    |
| Symptom {1}       | [Service cannot be started](#symptom_1) ValidationException is thrown.|

<a name="solution_invalid-arguments"></a>
#### Solution
Check the README.md to see the REQUIRED and examples of them. Make sure there are no typos.

<a name="verification_invalid-arguments"></a>
#### Verification
Request to /\__gtg endpoint of the service returns OK.


<a name="movie-graphdb-health-check-returns-status-error"></a>
### Movie GraphDB Health Check returns status ERROR

| #                 | Detail                                                         | 
|:----------------- |:---------------------------------------------------------------|
| Cause             | One or more of GraphDB health checks fail.                     |
| Symptom {1}       | [Movie GraphDB Health Check returns status ERROR](#symptom_3)  |
| Symptom {2}       | [Movie plot endpoint returns status code 500](#symptom_5)      |
| Symptom {3}       | [Movie endpoint returns status code 500](#symptom_7)           |
| Symptom {4}       | [Movies endpoint returns status code 500](#symptom_8)          |   

<a name="solution_movie-graphdb-health-check-returns-status-error"></a>
#### Solution
Check the response from /\__health to see which health check or checks fail. 
To resolve the errors take a look at the [documentation](http://graphdb.ontotext.com/documentation/enterprise/database-health-checks.html)
of GraphDB dedicated to this.


<a name="verification_movie-graphdb-health-check-returns-status-error"></a>
#### Verification
GraphDB Health Check should return status OK.


<a name="movie-graphdb-health-check-returns-status-warning"></a>
### Movie GraphDB Health Check returns status WARNING

| #                 | Detail                                                         | 
|:----------------- |:---------------------------------------------------------------|
| Cause             | One or more of GraphDB health checks return status "yellow".   |
| Symptom {1}       | [Movie GraphDB Health Check returns status WARNING](#symptom_2)      |  

<a name="solution_movie-graphdb-health-check-returns-status-warning"></a>
#### Solution
Check the response from /\__health to see which health check or checks return status "yellow". 
To resolve the warnings take a look at the [documentation](http://graphdb.ontotext.com/documentation/enterprise/database-health-checks.html)
of GraphDB dedicated to this.

<a name="verification_movie-graphdb-health-check-returns-status-warning"></a>
#### Verification
Movie GraphDB Health Check returns status OK.


<a name="graphdb-health-check-fails"></a>
### GraphDB Health Check fails

| #                 | Detail                                                              | 
|:----------------- |:--------------------------------------------------------------------|
| Cause             | One or more of GraphDB health checks fail. Status is red or yellow. |
| Symptom {1}       | [GraphDB Health Check fails](#symptom_4)                            | 

<a name="solution_graphdb-health-check-fails"></a>
#### Solution
To resolve take a look at the [documentation](http://graphdb.ontotext.com/documentation/enterprise/database-health-checks.html)
of GraphDB dedicated to this.

<a name="verification_graphdb-health-check-fails"></a>
#### Verification
Movie GraphDB Health Check returns status green.


<a name="graphdb-unreachable"></a>
### GraphDB is unreachable

| #                 | Detail                                                         | 
|:----------------- |:---------------------------------------------------------------|
| Cause             | GraphDB is unreachable.                                        |
| Symptom {1}       | [Movie GraphDB Health Check returns status ERROR](#symptom_3)  |
| Symptom {2}       | [Movie plot endpoint returns status code 500](#symptom_5)      |
| Symptom {3}       | [Movie endpoint returns status code 500](#symptom_7)           |
| Symptom {4}       | [Movies endpoint returns status code 500](#symptom_8)          |  

<a name="solution_graphdb-unreachable"></a>
#### Solution
Make sure there are no typos in the URI. Check also the port.
Use ping, tracert and etc. to troubleshoot and see where on the network is the issue.

<a name="verification_graphdb-unreachable"></a>
#### Verification
Request to /__health endpoint returns status OK. 


<a name="graphdb-down"></a>
### GraphDB is down

| #                 | Detail                                                         | 
|:----------------- |:---------------------------------------------------------------|
| Cause             | GraphDB is down.                                               |
| Symptom {1}       | [Movie GraphDB Health Check returns status ERROR](#symptom_3)  |
| Symptom {2}       | [Movie plot endpoint returns status code 500](#symptom_5)      |
| Symptom {3}       | [Movie endpoint returns status code 500](#symptom_7)           |
| Symptom {4}       | [Movies endpoint returns status code 500](#symptom_8)          |  

<a name="solution_graphdb-down"></a>
#### Solution
Check the official GraphDB [documentation](http://graphdb.ontotext.com/documentation/standard/)
for information on how to solve it.
Or contact the creators using the [contacts.](http://graphdb.ontotext.com/documentation/standard/support.html)

<a name="verification_graphdb-down"></a>
#### Verification
Movie GraphDB Health Check returns status OK.


<a name="graphdb-cannot-connect-elasticsearch"></a>
### GraphDB cannot connect to Elasticsearch

| #                 | Detail                                                        |
|:----------------- |:--------------------------------------------------------------|
| Cause             | Elasticsearch is unreachable for GraphDB.                     |
| Symptom {1}       | [Movie GraphDB Health Check returns status ERROR](#symptom_3) |
| Symptom {2}       | [Movies endpoint returns status code 500](#symptom_8)         |

<a name="solution_graphdb-cannot-connet-elasticsearch"></a>
#### Solution
Look for any typos in the URI for Elasticsearch.
Check the connection between GraphDB and Elasticsearch using ping, tracert and etc. 
to locate the network problem and fix it.

<a name="verification_graphdb-cannot-connet-elasticsearch"></a>
#### Verification
Using ping command the packets transmitted from GraphDB to Elasticsearch should be equal to the packets received by Elasticsearch.


<a name="elasticsearch-down"></a>
### Elasticsearch is down

| #                 | Detail                                                        |
|:----------------- |:--------------------------------------------------------------|
| Cause             | Elasticsearch is down.                                        |
| Symptom {1}       | [Movie GraphDB Health Check returns status ERROR](#symptom_3) |
| Symptom {2}       | [Movies endpoint returns status code 500](#symptom_8)         |

<a name="solution_elasticsearch-down"></a>
#### Solution
It depends on the implementation of your Elasticsearch - if it is locally installed or you are
using cloud service. Address the problem according to your specific case.

<a name="verification_elasticsearch-down"></a>
#### Verification
Movie GraphDB Health Check returns status OK.


<a name="missing-graphdb-repository"></a>
### Missing GraphDB repository

| #                 | Detail                                                            | 
|:----------------- |:------------------------------------------------------------------|
| Cause             | Lacking GraphDB repository to operate with.                       |
| Symptom {1}       | [Movie GraphDB Health Check returns status ERROR](#symptom_3)     |
| Symptom {2}       | [Movie plot endpoint returns status code 500](#symptom_5)         |
| Symptom {3}       | [Movies endpoint returns status code 500](#symptom_8)             |  

<a name="solution_missing-graphdb-repository"></a>
#### Solution
Check for the existence of the argument sparql.repository . Check for any typos in the name of the repository.
Or if you need to create one follow the official [documentation.](http://graphdb.ontotext.com/documentation/free/creating-a-repository.html)

<a name="verification_missing-graphdb-repository"></a>
#### Verification
Calling the /__health endpoint should not result in throwing a Repository Exception with message "Failed to get server protocol; no such resource on this server...".

<a name="omdb-api-failed"></a>
### OMDb API failed

| #                 | Detail                                                            | 
|:----------------- |:------------------------------------------------------------------|
| Cause             | OMDb API failed (5XX Server Error).                               |
| Symptom {1}       | [Movie plot endpoint returns status code 502](#symptom_6)         |  

<a name="solution_omdb-api-failed"></a>
#### Solution
Check the official site for OMDb API and troubleshoot it [here](http://www.omdbapi.com/)
If it does not help, contact them via email on bfritz@fadingsignal.com and wait until the issue is resolved.

<a name="verification_omdb-api-failed"></a>
#### Verification
Request to movie plot endpoint returns Status Code different than 502. 


<a name="invalid-apikey"></a>
### Invalid API key 

| #                 | Detail                                                            | 
|:----------------- |:------------------------------------------------------------------|
| Cause             | Wrong or Expired API key.                                         |
| Symptom {1}       | [Movie plot endpoint returns status code 500](#symptom_5)         |

<a name="solution_invalid-apikey"></a>
#### Solution
Make sure to get rid of any typos if present in the API key provided to the service.
In case you are unable to fix it get new one for free from [here.](http://www.omdbapi.com/apikey.aspx)

<a name="verification_invalid-apikey"></a>
#### Verification
The Service returns Status Code different than 500. 