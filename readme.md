# Spring Framework demonstration

A simple API that demostrates various usages of spring framework. It exposes API for registering/signin users,
confirming user registration, posting blog posts, as well as adding new tags and categories.
Users are authenticated with JSON Web Tokens (JWT).
Endpoint accessibility configuration is based on user authentication.

## Getting Started

These instructions will get you up and running with project in no time.

### Installing

After cloning the repo run `docker-compose up` in order to start mailhog and activemq. Mailhog is used as fake SMTP server for registration mails. After the container is up, you can access it on `http://localhost:8025/`. ActiveMQ is used as message broker to separate user and email service. Currently both services reside in the same app. You can access the ActiveMQ client on `http://localhost:8161/`.

To start the application run:

```
mvn clean install

mvn spring-boot:run
```

The app si visible at `http://localhost:8000`

The in-memory database can be accessed on `http://localhost:8000/h2/`
Make sure you are using `jdbc:h2:mem:testdb` as JDBC URL.

## Running the tests

```
mvn verify
```
or
```
mvn clean test
```

The report jacoco generated after the tests are executed can be found under
```
/target/jacoco-report/
```
Open `index.html` in a browser to see the report.

The `mvn jacoco:report` will generate report under `target/site/jacoco/`.

## Postman

Under the `/postman` directory you can find postman collection as well as Local postman environment exports.
Importing both of those files into Postman will give you prepared Postman requests to test endpoints.
Be aware that the requests under the `Authorization required` need the bearer token set under Local environment. The
first `Signin` request handles the jwt in response and sets it to environment variable, so other authorized requests
could be used.

## Built With

* [Spring Framework](https://spring.io/)
* [Maven](https://maven.apache.org/)
* [ActiveMQ](https://activemq.apache.org/)
* [Docker Compose](https://docs.docker.com/compose/)

## License

This project is licensed under the MIT License.
