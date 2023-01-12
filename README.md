# Sample REST CRUD API with Spring Boot, Postgresql, JPA and Liquibase. Covered with integration and unit tests with JUnit and Mockito
---
## How to Run Application:
---
#### 1. Clone the application
```bash
https://github.com/44x44/WeatherApp.git
```
#### 2. Create Postgresql database
```bash
create database weather_app_db
```
#### 3. Change postgresql username and password as per your installation
* remove `.origin` from the file name `src/main/resources/application.properties.origin`
* open `src/main/resources/application.properties`
* type `spring.datasource.username` and `spring.datasource.password` as per your postgresql installation

#### 4. Build and run the app using maven
```bash
mvn package
java -jar target/WeatherApp-0.0.1-SNAPSHOT.jar
```
Alternatively, you can run the app without packaging it using -
```bash
mvn spring-boot:run
```
The app will start running at http://localhost:8080.

---
### Before running integration tests:
---
#### 1. Create Postgresql database for testing
```bash
create database weather_app_testdb
```
#### 2. Change postgresql username and password in test properties
* remove `.origin` from the file name `src/test/resources/application-test.properties.origin`
* open `src/test/resources/application-test.properties`
* type `spring.datasource.username` and `spring.datasource.password` as per your postgresql installation
