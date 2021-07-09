# auth-svc
Quickly deliver authentication and authorization microservice in [Spring](https://spring.io/).

## Description
The goal is to expose some RESTful APIs to provide a lightweight and concise [JSON Web Token](https://jwt.io/) (JWT) authentication and authorization component.

The APIs can be divided into two groups: Authentication and Configuration.

### Authentication
The goal of the Authentication group is to provide the operations of register and login.
It is possible to store an account, i.e. an object composed by username and plain password, on [MongoDB](https://www.mongodb.com/) encrypting the given password through *authentication/register* API. Performing the login is very simple, it boils down to call an API, which is *authentication/login*, providing an account as a request body. It checks if an account exists on MongoDB, through a [Spring Security](https://spring.io/projects/spring-security/) mechanism, and then it will generate a JWT, which is crucial for consuming Configuration APIs.

### Configuration
The goal of Configuration APIs is handling information about saved accounts.
> NOTE: Consuming APIs that are not part of the Authentication group leads to perform some logic of a new HTTP filter, which check if a valid JWT exists in the request header.

It is possible to get and store information invoking *configuration/{username}* and *configuration* APIs. It is required to pass the JWT as a Bearer token into the request header.

## RESTful APIs

| HTTP Verb  | Endpoint | Request body | Goal |
| ------------- | ------------- | ------------- | ------------- |
| POST  | authentication/register | { "username" : "usr", "password" : "pwd" } | Save account on MongoDB
| POST  | authentication/login | { "username" : "usr", "password" : "pwd" } | Check if the account exists on MongoDB, then get JWT
| GET  | configuration/{username} | None | Get information about *username*
| POST  | configuration | { "username" : "usr", "description" : "Who I Am", "preferences" : "What I like"} | Save information for *username*
