# test-task-for-clear-solutions

**User Management API**

This API provides endpoints for managing user data.

Endpoints

**GET /api/users/all**

Description: Retrieve all users.
Response: List of all users.
HTTP Status Code: 200 OK

**POST /api/users/create**

Description: Create a new user.
Request Body: User object.

**Every user must be older than 18 years old**

Response: Newly created user.
HTTP Status Code: 200 OK

**PUT /api/users/update/{userId}**

Description: Update an existing user.
Path Variable: userId (ID of the user to be updated)
Request Body: Updated user object.
Response: Updated user.
HTTP Status Code: 200 OK

**PUT /api/users/updateAllFields/{userId}**

Description: Update all fields of an existing user.
Path Variable: userId (ID of the user to be updated)
Request Body: User object with all fields.
Response: Updated user.
HTTP Status Code: 200 OK

**DELETE /api/users/delete/{userId}**

Description: Delete an existing user.
Path Variable: userId (ID of the user to be deleted)
Response: Deleted user.
HTTP Status Code: 204 No Content

**GET /api/users/getInRange**

Description: Retrieve users within a specified date range.
Query Parameters:
from (required): Start date of the range (format: yyyy-MM-dd)
to (required): End date of the range (format: yyyy-MM-dd)

**From must be before to**

Response: List of users within the specified date range.
HTTP Status Code: 200 OK

**Technologies Used**

Spring Boot 3.2.5

Java 17

Lombok 1.18.30

TestContainers 1.19.7

Jackson (for JSON serialization/deserialization)

Mockito (for mocking dependencies in tests)

**Usage**

To use this API, you can send HTTP requests to the specified endpoints using a tool like Postman or curl.

Example request to retrieve all users:

**GET /api/users/all**

No need to use @RequestBody

**POST /api/users/create**

Example request to create a new user:

Request Body:
```json
{
    "id": 1,
    "email": "user665@example.com",
    "firstName": "John",
    "lastName": "Doe",
    "birthDate": "1990-01-15",
    "address": "123 Main St",
    "phoneNumber": "+12345678"
}
```
**PUT /api/users/update/{userId}**

{userId} : userId is @PathVariable that indicates user to be updated

Example request to update user by one/some field/fields:

Request Body:
```json
{
    "lastName": "Bobson"
}
```
**PUT /api/users/updateAllFields/{userId}**
{userId} : userId is @PathVariable that indicates user to be updated

Example request to update user by all field:

Request Body: 
```json
{
    "id": 1,
    "email": "user444@example.com",
    "firstName": "Bob",
    "lastName": "Dolly",
    "birthDate": "1999-03-19",
    "address": "555 Main Street",
    "phoneNumber": "+987654321"
}
```
**GET /api/users/getInRange?from=1989-04-01&to=1999-04-30**

from=1989-04-01&to=1999-04-30 : parameters that indicate the range in which we are looking for users with the corresponding date of birth

No need to use @RequestBody

**DELETE /api/users/delete/{userId}**

{userId} : userId is @PathVariable that indicates user to be deleted

No need to use @RequestBody

=======
# test-task-for-clear-solutions
>>>>>>> fea75413ea36c1ce78f49c09eba007a696c5995a
