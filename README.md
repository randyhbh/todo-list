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


### PATCH Endpoint

See [zalando restful api guidelines for PATCH](https://opensource.zalando.com/restful-api-guidelines/#patch)

### GET Endpoint
See [zalando restful api guidelines for GET](https://opensource.zalando.com/restful-api-guidelines/#get)