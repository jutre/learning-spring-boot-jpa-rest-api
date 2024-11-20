# Learning Hibernate, Spring-boot
### About this repository
This repository contains code created while learning Hibernate, JPA and Spring-boot libraries for creating REST API.
All types of ORM mappings except many-to-many have been implemented, all methods in Java code for making CRUD operations in database (JPA method naming, HQL queries, entity manager session, projections) implemented, appropriate REST API methods were implemented.

Manual REST API testing requests are located in requests.txt file. The file lists REST API requests for CURL program.
Automatic REST API tests are located in src/test/java/com/example/springbootrestapi/ directory.
Initial data is stored in database by code in src/test/java/com/example/springbootrestapi/student/StudentConfig.java file.

### Applications domain model
Classes in application's code reflects relations between following objects:
- Student has one StudentDelails object (one to one) - bidirectional mapping implemented
- Student has one birth Country (many to one) - unidirectional mapping implemented, Student object containing reference to Country object
- Student can have many PhotoImage objects (one to many) - bidirectional mapping implemented

### REST API specification
The REST API is quite simple and can be easily understood by requests stored in requests.txt file that was created for testing the application. The file contains REST API request commands for CURL program.
