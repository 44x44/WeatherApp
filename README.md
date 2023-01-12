# Sample REST CRUD API with Spring Boot, Postgresql, JPA and Liquibase. Covered with integration and unit tests with JUnit and Mockito
---
## How to Run Application:
---
#### 1. Clone the application
```bash
git clone https://github.com/44x44/WeatherApp.git
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
First, open the console and go to the folder with the cloned repository.

Then run (for Unix use `./mvnw` instead of `mvnw`):
```bash
mvnw package
java -jar target/WeatherApp-0.0.1-SNAPSHOT.jar
```
Alternatively, you can run the app without packaging it using
```bash
mvnw spring-boot:run
```
The app will start running at http://localhost:8080.


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

---
## About the service
---

Let's imagine that we have a weather sensor (for simplicity, let's just call it a "sensor"). This sensor once a day measures the temperature of the ambient air and determines whether it is raining or not.
The sensor has internet access, so it can send HTTP requests to our server.
Every time it takes a measurement, it will send an HTTP request with data in JSON format to our server - to do this in real life, we would tell the device the IP address of the computer where we run the Spring REST API application. Then, on our computer, we would be able to receive requests from the sensor.

Our service is a simple REST service with which we can :
1. Register the sensor in the system.
   For example the name information of the sensor is enough (but the names of sensors can't be repeated)
2. Receive measurements from the sensor
   Each measurement includes the following information:
   * value - ambient air temperature at the time of measurement
   * rainfall - in true or false format
   * information about the sensor that sent the measurement
3. Show the user information about all the measurements taken
4. Show the user the number of rainy days over the entire measurement period

### The app defines following CRUD APIs

#### Register a sensor
```bash
POST /sensors/registration
Content-Type: application/json

{
  "name": "Sensor name"
}

RESPONSE: HTTP 200 (OK)
```

#### Add a measurement
```bash
POST /measurements/add
Content-Type: application/json

{
  "value": 12.7
  "raining": false
  "sensor": {
    "name": "Sensor name"
  }
}

RESPONSE: HTTP 200 (OK)
```

#### Get all measurements list
```bash
GET /measurements

Response: HTTP 200
Content: list of JSON measurement objects
```

#### Get rainy days count
```bash
GET /measurements/getRainyDaysCount

Response: HTTP 200
Content: JSON object containing just 1 number field "rainyDaysCount"
```
