<h1 align="center">
	SpringBoot Project Template
</h1>
<p align="center">
	<a href="https://spring.io/projects/spring-boot">
		<img src="https://img.shields.io/badge/SpringBoot-v2.1.6-brightgreen">
	</a>
	<a href="https://opensource.org/licenses/MIT">
		<img src="https://img.shields.io/badge/License-MIT-green">
	</a>
</p>

Helpful (hopefully) SpringBoot template that support basic needs such as:
- CRUD to database using `JpaRepository`
- Authentication and authorization with Spring security + JSON Web Token
- Seeder to initialize data on your database

## Modules
List of modules

| Module Name   | Description                                                     |
|---------------|-----------------------------------------------------------------|
| application   | SpringBoot main application class and properties file           |
| configuration | Configuration classes including injected bean and security      |
| entity        | Database model classes                                          |
| enumeration   | Enums                                                           |
| exception     | Exception classes                                               |
| helper        | Helper classes                                                  |
| properties    | Spring properties classes                                       |
| repository    | Database repository classes                                     |
| security      | Security related classes including auth filter logic and JWT    |
| seeder        | Database seeder classes                                         |
| service       | Service interfaces to be injected                               |
| service-impl  | Classes of service layer including main logic of every endpoint |
| service-model | Model classes for service layer                                 |
| web           | Web controller classes to serve endpoint                        |
| web-model     | Model classes for controller layer                              |


## How To
### Choose spring profile
Add `-Dspring.profiles.active=<PROFILE>` on your VM options, for example `-Dspring.profiles.active=dev`


### Activate or deactivate database seeder
To activate database seeder, you must use `dev` profile and set `app.config.database.seeder-active=true` on properties
