# auth-svc
![JaCoCo 100% coverage](https://img.shields.io/badge/coverage-100%25-brightgreen)

## Description
"auth-svc" is a lightweight authentication and authorization microservice written in [Java 8](https://www.oracle.com/java/technologies/java8.html) using [Spring](https://spring.io/).

Its goal is to expose some RESTful APIs to provide a [JSON Web Token](https://jwt.io/) (JWT) sign-up and sign-in components. There are two groups of APIs: Authentication and Configuration.

## Authentication
The goal of Authentication APIs is to provide the operations of sign-up and sign-in.

### Sign-up
You can save on [MongoDB](https://www.mongodb.com/) a new account providing an object composed of
an authentic username and a plain password.
Through *authentication/register* API, it encrypts the password and creates an account on DB.

### Sign-in
You can invoke an API, i.e. *authentication/login*, providing an existing account for sign-in operation. It checks the account existence on DB through a
[Spring Security](https://spring.io/projects/spring-security/) mechanism, and then it will generate and get a JWT, which is crucial for consuming Configuration APIs.

## Configuration
The goal of Configuration APIs is handling preferences about saved accounts.
> NOTE: Calling Configuration APIs leads to invoking a custom *HTTP filter*. It checks if a valid JWT exists in the request header.

You can get or save account information invoking *configuration/{username}* or *configuration* APIs.
Being inside the system, you need to pass a valid JWT as a *Bearer token* into the request header.

## Table of RESTful APIs 

| HTTP Verb  | Endpoint | Request body | Goal |
| ------------- | ------------- | ------------- | ------------- |
| POST  | authentication/register | { "username" : "usr", "password" : "pwd" } | Save a new account on DB
| POST  | authentication/login | { "username" : "usr", "password" : "pwd" } | Check if the account exists on DB, then get a JWT
| GET  | configuration/{username} | None | Get account information by *username*
| POST  | configuration | { "username" : "usr", "description" : "Who I Am", "preferences" : "What I like"} | Save account information
