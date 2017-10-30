# sample_springboot_data_api
This project is used to test different functionality of a JPA enabled springboot application. It also goes thru simple security scenarios for restricting access to certain resources.

It has been used to test overriding dependencies in the springboot starter POMs to document how disruptive (or not) upgrading versions of dependencies may (or may not) be. For example, upgrading the version of Hibernate, log4j2, etc from the versions included in the springboot starters.

It is pretty much just a playground or sandbox for testing and learning different functionality outside of real world APIs.

Two main APIs are present in the application:
1. People API (using JPA Repository and Entity)
2. Persons API (using REST Controller calling the JPA repository

Both APIs were built using the Person Entity as the primary object with several functional aspects being tested:
1. Testing READ_ONLY, WRITE_ONLY and READ_WRITE properties
2. ZonedDateTime properties and how to properly use them with JSON and JPA
3. findBy... repository methods

First attempt at securing resources was also included.
1. Securing API resource paths
2. Setting springboot actuator path contexts and enabling security via ROLE_
3. Basic Authentication vs. Form Authentication (starting to evaluate LDAP or another more secure form of authenticating user requests)

### To test this API (after cloning and importing):
##### 1. Sample POST request to insert multiple people into the H2 database using REST Controller resource path /persons

```http
POST /persons HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache

[
	{
		"firstName":"John",
		"lastName":"Smith",
		"birthDate":"1981-12-18", 
		"lastUpdated":"2017-10-24T22:10:15-05:00"
	},
	{
		"firstName":"Jane",
		"lastName":"Smith",
		"birthDate":"1984-02-29", 
		"lastUpdated":"2017-11-04T02:34:15-05:00"
	},
	{
		"firstName":"Joe",
		"lastName":"Smith",
		"birthDate":"1992-07-04", 
		"lastUpdated":"2001-01-01T00:34:15-06:00"
	},
	{
		"firstName":"Jim",
		"lastName":"Smith",
		"birthDate":"2001-01-08", 
		"lastUpdated":"2013-05-04T19:34:15-06:00"
	}
]
```

##### 2. Sample POST request to insert single person into the H2 database using JPA Repository resource path /people
```http
POST /people HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache

{
	"firstName":"Jim",
	"lastName":"Smith",
	"birthDate":"2001-01-08", 
	"lastUpdated":"2013-05-04T10:34:15-06:00"
}
```

##### 3. Sample PUT request to update a single person by resource id using JPA Repository resource path /people/{id}
```http
PUT /people/1 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Cache-Control: no-cache

{
	"firstName": "John",
	"lastName": "Smith",
	"birthDate": "1981-12-18",
	"lastUpdated": "2017-10-24T22:10:15-05:00"
}
```

##### 4. GET request to retrieve people records, using JPA Repository, updated between two dates /people/search/findByLastUpdatedBetween?...
```
http://localhost:8080/people/search/findByLastUpdatedBetween?startDate=2017-11-01T00:00:00Z&endDate=2017-11-04T08:00:00Z
```

##### 5. GET request to retrieve people records, using JPA Repository, updated on or after a date /people/search/findByLastUpdatedGreaterThanEqual?...
```
http://localhost:8080/people/search/findByLastUpdatedGreaterThanEqual?startDate=2011-11-01T00:00:00Z
```

##### 6. GET request to retrieve ALL people records, using REST Controller resource path /persons
```
http://localhost:8080/persons
```

##### 7. GET request to retrieve ALL people records, using JPA Repository resource path /people
```
http://localhost:8080/people
```