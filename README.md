# http4s-crud-example
Small crud example using http4s, doobie, circe
# Stack
* Http4s for http streaming
* Circe for JSON serialization
* Doobie fro Database access
* Cats for FP
* H2 Database for storing data. 


# Architecture
## Domain
All domain related login kept here 
## Infrastructure
All infra related logic is kept her. 
* /db -> Database communication
* /endpoints -> Http communication


To run : sbt run

## Example commands for PostOffice(GET, POST, PUT, DELETE):

curl -i http://localhost:8080/offices/1
curl -i http://localhost:8080/offices
curl -v -H "Content-Type: application/json" -X POST http://localhost:8080/offices -d '{"zip":"123456", "name":"Baker Street"}'
curl -v -H "Content-Type: application/json" -X PUT http://localhost:8080/huts -d '{"id":"1","zip":"123456","name":"Mountain View"}'
curl -v -X DELETE http://localhost:8080/offices/1

## Example command for Shipments(GET, POST, PUT, DELETE): 
curl -i http://localhost:8080/shipments/1
curl -i http://localhost:8080/shipments
curl -v -H "Content-Type: application/json" -X POST http://localhost:8080/shipments -d '{"officeId":"1", "shipmentType":"Letter"}'
curl -v -H "Content-Type: application/json" -X PUT http://localhost:8080/shipments -d '{"id":"1","officeId":"1","shipmentType":"Package"}'
curl -v -X DELETE http://localhost:8080/shipments/1
