## Code Structure

The service strives to follow the clean architecture approach

![clean architecture diagram](docs/clean_architecture.png)

(source: https://medium.com/swlh/clean-architecture-a-little-introduction-be3eac94c5d1)

All API endpoints are located in `com.ss.challenge.todolist.api` package. The OpeanAPI documentation for the API is available in [openapi.yaml](openapi.yaml).

Interfacing with external systems (ex: databases) is limited to `com.ss.challenge.todolist.infra` package.

### Important to notice
Use-cases are located in `com.ss.challenge.todolist.usecases` contain the application logic and depend only on domain entities. 
To shorten the implementation and reduce the complexity, I'm using on the domain entity the JPA annotations and Spring Data JPA to take care of the database queries,
if we were to follow the clean architecture to the letter we would need to have an entity that represents our domain and one that represents our persistence layer having a Repository interface
and then having the implementation

## Service functionalities explanation

### add an item
### change description of an item
### mark an item as "done"
### mark an item as "not done"
### get all items that are "not done" (with an option to retrieve all items)
### get details of a specific item
### The service should automatically change status of items that are past their due date as "past due".

### Assumptions and design considerations, and validations left out
- An item can only be created from the API and using the defined use-case follow this validations:
    - createdAt date is smaller than the dueAt date
- if an item where to be created manually from any other part of the code, the validations done on the API would have to
  be added to the entity class or to any other common point (`this was left out`)

### PATCH Endpoint
See [zalando restful api guidelines for PATCH](https://opensource.zalando.com/restful-api-guidelines/#patch)

### GET Endpoint
See [zalando restful api guidelines for GET](https://opensource.zalando.com/restful-api-guidelines/#get)