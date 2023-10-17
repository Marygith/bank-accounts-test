# RESTful api for bank accounts

## Main features

- Bank account can be created by supplying a name and four-digit PIN code.
- Once account is created one can Deposit, Withdraw or Transfer money between accounts.
- Deduction from account requires correct pin.
- A specific call allows to fetch information about all existing accounts.
- APIs using JSON payloads when applicable.
- In-memory database is used as a backing store.

## Endpoints

#### http://localhost:8080/accounts/getAll [GET] 
#### http://localhost:8080/accounts/addAccount [POST] 
#### http://localhost:8080/accounts/withdraw [POST]
#### http://localhost:8080/accounts/deposit [POST]
#### http://localhost:8080/accounts/transfer [POST]

## Tech stack
- Java 17
- Spring Boot 3.1.4

## Getting started

./mvnw clean install 

java -jar target/aston_test-0.0.1-SNAPSHOT.jar