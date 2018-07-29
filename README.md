# workspace
A common workground for learning new technlogies

Blog application:

This application is built using Spring Boot, Thymeleafs and Bootstrap.

Prerequisites:

You need to install Postgres and then create a database called blog.

Open Blog\src\main\resources\config\application.properties and provide a profile(prod or dev).

Open Blog\src\main\resources\application-(prod, dev).properties and provide user name and password for Postgres.

By default the application is running on port 4000 which can be changed by modifying the property server.port=4000 in the Blog\src\main\resources\application-(prod, dev).properties file.

Install Java 8 and Maven.

How to build:
Go the Blog directory and then run the target (mvn install) to build the application or target (mvn spring-boot:run) to run the application.

There is no need to install Tomcat or any other Web Server.

The application comes with two builtin users.

Admin (username: vickyrare@gmail.com, password: 12345678)
User (username: vickyrare@yahoo.com, password: 12345678)

They can be changed in the src/main/resources/static/db/postgres-data.sql file.
