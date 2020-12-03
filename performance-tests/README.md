Performance tests are made respectively to satisfy a set of [non-functional requirements](https://gitlab.ontotext.com/SAS/k9/blob/master/architecture.md#non-functional-requirements) 
in order for the API to be responsive enough.


## Parameters that can be configured ##
* server (default: 127.0.0.1) -> the server to which the requests to be made
* port (default: 8888) -> the port to which the requests on the sever to be made
* path (default: /movienator-api) -> the path to the application on the server
* numThreads (default: 10) -> the number of users (threads) to make requests simultaneously
* rampUp (default: 1) -> the period in which this users (threads) request simultaneously from the server
* loopCount (default: 1000) -> how many times the above configuration to be repeated
* csvfile (default: movies.csv) -> csv file with three columns movieId, moviePlotId and query respectively
passed to /movies/movieId, /movies/moviePlotId/plot and /movies?query endpoints. You can pass your custom csv file
or use the default one.


## What to know before testing? ##
When you test /movies/moviePlotId/plot endpoint, you are inserting new triples in the database.
These triples are for plots. So if you want to reset your database, so that the calls to the OMDP API
to be made again you have two options:
  1. Restart the container. This way you erase all the data created during the container's life...
  2. Connect to the container and execute the following example query to delete all the plots created on it.
  IMPORTANT: Make sure you changed the values according to your configuration.
  (If you've mapped the ports you can execute the query from your host. Otherwise you can just specify
  the GraphDB IP and port used and again execute the query.)
  curl -X POST -H 'Content-Type: application/x-www-form-urlencoded' --data-urlencode 
                                  update='PREFIX mdb: <http://example.org/movieDB/>
                                          DELETE WHERE {
                                              ?movie mdb:plot ?plot
                                          }' 
                                          'http://localhost:7200/repositories/movieDB/statements'
  3. You can connect to the GraphDB via your browser and use the GUI. Then you will have the possibility to
  delete the plots created manually or executing the query from 2. in the SPARQL section.

## Run JMeter tests using maven ## (Preferred)
No need to download JMeter. JMeter Maven Plugin is used with version 2.8.6
from [the official maven repository](https://mvnrepository.com/artifact/com.lazerycode.jmeter/jmeter-maven-plugin).

Navigate to the base directory of performance-tests module where the pom.xml file is located.
Run "mvn verify -P performance-tests" or "mvn clean verify -P performance-tests" 
if /target directory is already present under /performance-tests.
(-P performance-tests: this way you specify the profile you want to run in.
The default profile does not trigger the tests.)

You can also configure every of the arguments or combination of them as follows (these are the default values):
mvn clean verify -P performance-tests -Dserver=127.0.0.1 
                                      -Dport=8888 
                                      -Dpath=/movienator-api 
                                      -DnumThreads=10 
                                      -DrampUp=1 
                                      -Dcsvfile=movies.csv
                                      -DloopCount=1000

#### Find results ####
Again in the base directory of this module you should find /target directory.
Navigate to {basedir_of_the_module}/target/jmeter/results. There you should find .csv file.
Open it in editor of your choice and take a look at the results.


## Run JMeter tests from JMeter ##
Download JMeter [the official web site](https://jmeter.apache.org/download_jmeter.cgi).
This example is with JMeter 5.1.1 version.

To run the tests navigate to the directory where jmeter executable file is located on your machine. 
In this case ~/apache-jmeter-5.1.1/bin/jmeter

Then type jmeter -n -t {test-plan.jmx} -l {results.jtl}
-n -> run jmeter in NON GUI mode
-t -> specify the file with the configuration for testing
-l -> specify the file where you want to record the results from the tests

You can also configure every of the arguments or combination of them as follows (these are the default values): 
./jmeter -n -t {relative_path}/movienator-performance-tests-with-parameters.jmx 
                                              -Jserver=127.0.0.1 
                                              -Jport=8888 
                                              -Jpath=/movienator-api 
                                              -JnumThreads=10 
                                              -JrampUp=1 
                                              -JloopCount=1000
                                              -Jcsvfile=movies.csv 
                                              -l results.jtl

## Make customizations ##
To make customizations you can open the movienator-performance-tests.jmx file in JMeter.
All configured fields are stated below. Feel free to change all of them according to your use case.

Go to Users. On the right you will see Thread properties paragraph. There you will find:
* Number of Threads(users) is set to 10. (Number of separated users to make calls.)
* Ramp-Up Period (in seconds) is set to 1. (The time period for all of the requests to be )
* Loop Count set to 1000. (The time to repeat the above configuration.)

After this on every different request under Users:
* Server Name or IP is set 127.0.0.1 (Tests are executed against localhost, so the default server ip for all tests is 127.0.0.1,
but you can change it in the field written as Server Name or IP.)
* Method is set to GET or PATCH according to the endpoint allowed methods.

( Please note that the default path and port number are the same for all of the endpoints and if changed you will no longer test the API.)
( You still are allowed to change the {rest-of-endpoint} according to the endpoint you want to test.)
* Path is set to /movienator-api/{rest-of-endpoint} (/movienator-api is the default location of the api on the server)
* Port is set to 8888


