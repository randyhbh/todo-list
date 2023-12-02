## Code Structure

The service strives to follow the clean architecture approach

![clean architecture diagram](docs/clean_architecture.png)

(source: https://medium.com/swlh/clean-architecture-a-little-introduction-be3eac94c5d1)

All API endpoints are located in `com.ss.challenge.todolist.api` package. The OpeanAPI documentation for the API is
available in [openapi.yaml](openapi.yaml).

Interfacing with external systems (ex: databases) is limited to `com.ss.challenge.todolist.infra` package.

### Important to notice
Use-cases are located in `com.ss.challenge.todolist.usecases` contain the application logic and depend only on domain 
entities. 

To shorten the implementation and reduce the complexity, I'm using on the domain entity the JPA annotations and Spring 
Data JPA to take care of the database queries, if we were to follow the clean architecture to the letter we would need
to have an entity that represents our domain and one that represents our persistence layer having a Repository interface
and then having the implementation

The service do not handle timezones and even if it would be easy to work always with UTC would be confusing for the person
reviewing this, since it would have to convert his current timezone to UTC and back hence I didn't include it in the implementation,
although it could be done somewhat fast with the current structure/implementation.

## Service functionalities explanation

### Add an item
When adding an new item, having a description and a due date is mandatory and an item can not be created without it.
The initial `status` will be `NOT_DONE` and the created date will be value of calling `LocalDateTime.now()`

### Change description of an item
When wanting to change the description of an already existent item, providing a new value is mandatory and no empty or null
values are accepted.

You wont be allowed to change the description of items with status in `PAST_DUE` or `DONE`

If the item does not exist a response telling this is returned.

### Mark an item as "done"
When completing an item we only need to provide the item Id. An item can only be set done if his `status!=PAST_DUE`.
Performing the same call to the API to complete the same item will produce the same result.

If the item does not exist a response telling this is returned.
The violation of this conditions will cause and exception/response to be returned explaining the problem. 

### Mark an item as "not done"
When re-opening an item we only need to provide the item Id. An item can be re-opened if his `status!=PAST_DUE`, and
the `dueDate is before re-opening date`. Performing the same call to the API to re-open the same item will produce 
the same result.

If the item does not exist a response telling this is returned.
The violation of this conditions will cause and exception/response to be returned explaining the problem.

### Get all items that are "not done" (with an option to retrieve all items)
This functionality default behaviour is to return all items with `status=NOT_DONE` or an empty List if none is found. If the 
non required boolean flag `includeAllStatuses` is passed with value `true` the all items without considering the `status`
are returned or empty is List if none is found.

### Get details of a specific item
When requesting the details of an item if the item does not exist a response telling this is returned.

### The service should automatically change status of items that are past their due date as "past due".
We achieve this in the current implementation by having a scheduled task that checks all created items with 
`status=NOT_DONE` and `due date before now()` and set to past due the matching ones. This task is executed every 5s.

#### Important notice about the scheduled task using @Scheduler from Spring Boot
This solution is not considering scalability, if we were to deploy this in a distributed environment where we have 
multiple instances of our application, we need to ensure the scheduler synchronization over multiple instances and 
Spring Scheduler cannot handle this, instead , it executes the jobs simultaneously on every node.

If this were a requirement we could achieve it by using ShedLock library. It ensures our scheduled tasks when deployed
in multiple instances are executed at most once at the same time. It uses a locking mechanism by acquiring a lock on 
one instance of the executing job which prevents the execution of another instance of the same job.

[See implementation](src/main/java/com/ss/challenge/todolist/usecases/expiration/apply/ApplyExpirationUseCase.java) 

## Considerations done when designing the API
I decided to use a PATCH for the complete and re-open endpoint, because PUT will indicate updating the entire resource
and POST will indicate creating a new resource.

I also used Zalando api guidelines to helped me to make this decision. [zalando restful api guidelines for PATCH](https://opensource.zalando.com/restful-api-guidelines/#patch)

## How to build the service
- 