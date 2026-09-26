----Setup----
Ensure to create a copy of .env.example. Rename to .env, and complete missing fields.
Load schema.sql. Than load seed.sql.

----Layers----
The backend is divided into multiple layers.

1.Index (entry point)
First the backend is reached through the index.php file which then gives the router to handle the request.

2. Router
The router extracts the relevant data from the url to route to correct controller + action.
It handles middleware authentication, unless it is in the exception list.
It than calls the respective controller and action.
It than also handles any errors that are thrown at lower layers, bubbling the error upwards.

3. controller
Responsible for getting data from request.
Responsible for sending back a response.
Responsible for doing basic authorization only (check if correct user role)

4. Service
Responsible for checking authorization checks
Responsible for calling the relevant repository or directly communicating to db.

5. Repository
Provides sql queries that can be used

6. Database
Handles the connection.
Provides necessary interface for querying and executing data.

----Additional resources----
-Exception handler
created to handle the exceptions.

-Exceptions
Declared for ease of reuse.

-Request
Created as helper class for extracting data from response.

-RequestContext
To attach the user info after token checked.

-JsonResponse
Created to ensure standard structure for php.-Env loader
To load environment variables

----Files----
-env 
is used to store secrets and .env.example is provided to populate whats needed

-gitignore 
is used to ignore uploading .env files

-.htaccess 
is used to configure apache to properly handle routing

----Changes when building on xampp----
changed router to exclude mywebsite url part
added .htaccess to prevent default xampp from routing


----AI analyse----
Broken up into competencies and skills.

Competencies will consist of
Communication
Technical
Problem solving

Problem solving question type = problem +technical + communicating
Technical question type = technical + Communication
Communication type = Communication

Competency breakdown gives sub criteria

Communication
Clarity
structure
introduction

Technical
Definition
Understanding
Application
Architecture
Implementation
Past experience

Problem solving
Trouble shooting
Root cause analysis
Risk awareness
Trade offs
Approach selection