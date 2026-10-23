# 1.Setup
1. Copy .env.example and rename the copy to .env.
2. Fill in the required environment variables in .env.
3. Import schema.sql into MYSQL to create database, note the database name should already exist.
4. Import seed.sql to populate the initial data.
5. Configure the project to run under Apache using XAMPP. 
6. For best development use virtual hosting to work
in the same folder as locally cloned repo.
7. Add path to config file to the .env file.

Ensure .env is not commited to version control, as sensitive credentials should not be available to public. 
If this does happen change .env to new credentials and remove from commit and pushes.

## 2.Structure
- **Index.php** Entry point for incoming requests
- **Router** resolves the requested controller and action,
and applies authentication middleware and handles errors.
- **Controller** extract request data, call authorisation checks and call services and return response.
- **Services** calls repository methods and returns response for controller. Does business checks such as ownership and other validation checks
- **Repositories** Encapsulate SQL queries to retrieve and modify data.
- **Database**. Manages the db connection and provided query execution functionality.

## 3.Supporting components.
- **Exception handler**: catches exceptions and converts to a json response
- **Exceptions**: Reusable class for specific error conditions.
- **Request**: extracts incoming request data.
- **RequestContext**: holds authenticated user info after token validation
- **JsonResponse**: consistent JSON response structure.
- **Environment loader**: loads config values
- **AutoLoad**: loads files automatically
- **Container**: performs dependency injection DI for better architecture
- **Log**: provided default logging mechanism to be reused.
- **TimeUtil**: used to convert to long standard time
- **Token**: used to generate tokens.

## 4.Tests.
Tests was written for API level to check if other endpoints were affected with changes.

Run the following command from main repository, not backend ./vendor/bin/phpunit
