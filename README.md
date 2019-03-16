#Flywithus
Application allows to manage reservations of flights.

Application is using in memory database which is reset during each application run.

##What you need
To build application you need jdk8 and maven.
To run application you need free port 8080 or you need to change configuration in src/main/resources/application.properties

##How to build application
mvn clean install

###How to access H2 Database UI
To access H2 UI it you need to:

open http://localhost:8080/h2-console website

use default settings but:
JDBC URL:	jdbc:h2:mem:testdb
User Name: sa
Password: sa

click Connect

Please note that this UI should be disabled on production environment.

##How to use
REST API is described using swagger: http://localhost:8080/swagger-ui.html

By default application is using testdata from file: \flywithus\src\main\resources\initial-data.sql
