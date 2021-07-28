# auth-svc
![JaCoCo 100% coverage](https://img.shields.io/badge/coverage-100%25-brightgreen)

## Description
Lightweight authentication and authorization microservice written in [Spring](https://spring.io/) using [Java 8](https://www.oracle.com/java/technologies/java8.html).

Its goal is to expose some RESTful APIs to provide a simple and concise [JSON Web Token](https://jwt.io/) (JWT) sign-up and sign-in components.

The APIs can be divided into two groups: Authentication and Configuration.

### Authentication
The goal of Authentication APIs is to provide the operations of sign-up and sign-in.

You can save an account, i.e. an object composed by username and plain password, on [MongoDB](https://www.mongodb.com/) encrypting the given password through *authentication/register* API.
Performing the login is simple, you can invoke an API, which is *authentication/login*, providing an account as a request body. It checks if an account exists on MongoDB, through a
[Spring Security](https://spring.io/projects/spring-security/) mechanism, and then it will generate a JWT, which is crucial for consuming Configuration APIs.

### Configuration
The goal of Configuration APIs is handling preferences about saved accounts.
> NOTE: Consuming APIs that are not part of the Authentication group leads to perform a custom *HTTP filter*, which checks if a valid JWT exists in the request header.

You can get or save account information invoking *configuration/{username}* or *configuration* APIs.
Being inside the system, you need to pass a valid JWT as a *Bearer token* into the request header.

## RESTful APIs

| HTTP Verb  | Endpoint | Request body | Goal |
| ------------- | ------------- | ------------- | ------------- |
| POST  | authentication/register | { "username" : "usr", "password" : "pwd" } | Save account on MongoDB
| POST  | authentication/login | { "username" : "usr", "password" : "pwd" } | Check if the account exists on MongoDB, then get JWT
| GET  | configuration/{username} | None | Get information about *username*
| POST  | configuration | { "username" : "usr", "description" : "Who I Am", "preferences" : "What I like"} | Save information for *username*