Sample REST CRUD API with Spring Boot, Mysql, JPA and Hibernate
How to Run Application
1. Clone the application

https://github.com/44x44/WeatherApp.git
2. Create Postgresql database

create database weather_app_db
3. Change postgresql username and password as per your installation

remove ".origin" from the file name src/main/resources/application.properties.origin

open src/main/resources/application.properties

type spring.datasource.username and spring.datasource.password as per your postgresql installation

4. Build and run the app using maven

mvn package
java -jar target/spring-boot-rest-api-tutorial-0.0.1-SNAPSHOT.jar
Alternatively, you can run the app without packaging it using -

mvn spring-boot:run
The app will start running at http://localhost:8080.