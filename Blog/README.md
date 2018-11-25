# Blog

###Maven setup
1. mvn clean install
2. java -jar target/blog-0.0.1-SNAPSHOT.jar
3. Or
4. mvn spring-boot:run 

- http://localhost:8080/registration
- http://localhost:8080/login

###Docker setup
1. Set spring.profiles.active=docker in Blog/src/main/resources/config/application.properties file
2. Go to workspace folder and type docker-compose up

- http://localhost/registration
- http://localhost/login
