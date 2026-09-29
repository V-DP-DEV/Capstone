INSERT INTO users (email,password,role) VALUES ('admin@example.com','$2y$10$\/0VAp1X3HiYiWX14lMCjZuqOzIkZY\/WtWemSM9ovx1ektl2.C4s3S','ADMIN');
INSERT INTO users (email,password,role) VALUES ('user@example.com','$2y$10$\/0VAp1X3HiYiWX14lMCjZuqOzIkZY\/WtWemSM9ovx1ektl2.C4s3S','USER');
INSERT INTO users (email,password,role) VALUES ('user2@example.com','$2y$10$\/0VAp1X3HiYiWX14lMCjZuqOzIkZY\/WtWemSM9ovx1ektl2.C4s3S','USER');

-- =========================================================
-- INTERVIEW CATEGORIES
-- =========================================================

INSERT INTO interview_categories (name)
VALUES ('Software Development');


-- =========================================================
-- COMPETENCIES
-- =========================================================

INSERT INTO competencies (name)
VALUES ('communication');

SET @competency_id = LAST_INSERT_ID();

INSERT INTO competency_breakdowns (competency_id, name)
VALUES (@competency_id, 'clarity');

INSERT INTO competency_breakdowns (competency_id, name)
VALUES (@competency_id, 'structure');

INSERT INTO competency_breakdowns (competency_id, name)
VALUES (@competency_id, 'introduction');


INSERT INTO competencies (name)
VALUES ('technical');

SET @competency_id = LAST_INSERT_ID();

INSERT INTO competency_breakdowns (competency_id, name)
VALUES (@competency_id, 'Definition');

INSERT INTO competency_breakdowns (competency_id, name)
VALUES (@competency_id, 'Understanding');

INSERT INTO competency_breakdowns (competency_id, name)
VALUES (@competency_id, 'Application');

INSERT INTO competency_breakdowns (competency_id, name)
VALUES (@competency_id, 'Architecture');

INSERT INTO competency_breakdowns (competency_id, name)
VALUES (@competency_id, 'Implementation');

INSERT INTO competency_breakdowns (competency_id, name)
VALUES (@competency_id, 'Past experience');


INSERT INTO competencies (name)
VALUES ('problem solving');

SET @competency_id = LAST_INSERT_ID();

INSERT INTO competency_breakdowns (competency_id, name)
VALUES (@competency_id, 'Trouble shooting');

INSERT INTO competency_breakdowns (competency_id, name)
VALUES (@competency_id, 'Root cause analysis');

INSERT INTO competency_breakdowns (competency_id, name)
VALUES (@competency_id, 'Risk awareness');

INSERT INTO competency_breakdowns (competency_id, name)
VALUES (@competency_id, 'Trade offs');

INSERT INTO competency_breakdowns (competency_id, name)
VALUES (@competency_id, 'Approach selection');


-- =========================================================
-- QUESTION TYPES
-- =========================================================

INSERT INTO question_types (name) VALUES ('Introduction');
INSERT INTO question_types (name) VALUES ('Definition');
INSERT INTO question_types (name) VALUES ('Conceptual');
INSERT INTO question_types (name) VALUES ('Implementation');
INSERT INTO question_types (name) VALUES ('Debugging');
INSERT INTO question_types (name) VALUES ('Architecture');
INSERT INTO question_types (name) VALUES ('Trade-off');
INSERT INTO question_types (name) VALUES ('Experience');


-- =========================================================
-- QUESTION TYPE -> COMPETENCY BREAKDOWNS
-- =========================================================

-- ---------------------------------------------------------
-- Introduction
-- communication:
-- clarity + structure + introduction
-- ---------------------------------------------------------

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Introduction'
  AND c.name = 'communication'
  AND cb.name IN ('clarity', 'structure', 'introduction');


-- ---------------------------------------------------------
-- Definition
-- communication: clarity
-- technical: Definition
-- ---------------------------------------------------------

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Definition'
  AND c.name = 'communication'
  AND cb.name = 'clarity';

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Definition'
  AND c.name = 'technical'
  AND cb.name = 'Definition';


-- ---------------------------------------------------------
-- Conceptual
-- communication: clarity + structure
-- technical: Understanding
-- ---------------------------------------------------------

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Conceptual'
  AND c.name = 'communication'
  AND cb.name IN ('clarity', 'structure');

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Conceptual'
  AND c.name = 'technical'
  AND cb.name = 'Understanding';


-- ---------------------------------------------------------
-- Implementation
-- communication: clarity + structure
-- technical: Implementation
-- ---------------------------------------------------------

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Implementation'
  AND c.name = 'communication'
  AND cb.name IN ('clarity', 'structure');

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Implementation'
  AND c.name = 'technical'
  AND cb.name = 'Implementation';


-- ---------------------------------------------------------
-- Debugging
-- communication: clarity + structure
-- problem solving:
-- troubleshooting + root cause + risk + approach
-- ---------------------------------------------------------

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Debugging'
  AND c.name = 'communication'
  AND cb.name IN ('clarity', 'structure');

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Debugging'
  AND c.name = 'problem solving'
  AND cb.name IN (
      'Trouble shooting',
      'Root cause analysis',
      'Risk awareness',
      'Approach selection'
  );


-- ---------------------------------------------------------
-- Architecture
-- communication: clarity + structure
-- technical: Architecture
-- problem solving: Trade offs + Approach selection
-- ---------------------------------------------------------

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Architecture'
  AND c.name = 'communication'
  AND cb.name IN ('clarity', 'structure');

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Architecture'
  AND c.name = 'technical'
  AND cb.name = 'Architecture';

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Architecture'
  AND c.name = 'problem solving'
  AND cb.name IN ('Trade offs', 'Approach selection');


-- ---------------------------------------------------------
-- Trade-off
-- communication: clarity + structure
-- problem solving: Trade offs + Approach selection
-- ---------------------------------------------------------

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Trade-off'
  AND c.name = 'communication'
  AND cb.name IN ('clarity', 'structure');

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Trade-off'
  AND c.name = 'problem solving'
  AND cb.name IN ('Trade offs', 'Approach selection');


-- ---------------------------------------------------------
-- Experience
-- communication: clarity + structure
-- technical: Past experience
-- ---------------------------------------------------------

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Experience'
  AND c.name = 'communication'
  AND cb.name IN ('clarity', 'structure');

INSERT INTO question_type_competency_breakdowns
(question_type_id, competency_breakdown_id)
SELECT qt.id, cb.id
FROM question_types qt
JOIN competency_breakdowns cb
JOIN competencies c ON c.id = cb.competency_id
WHERE qt.name = 'Experience'
  AND c.name = 'technical'
  AND cb.name = 'Past experience';


-- =========================================================
-- CONCEPTS
-- =========================================================

INSERT INTO concepts (name) VALUES
('csharp-basics'),
('dependency-injection'),
('constructor-injection'),
('loose-coupling'),
('async-await'),
('http-client'),
('blazor-components'),
('component-lifecycle'),
('frontend-performance'),
('network-performance'),
('authentication'),
('authorization'),
('access-tokens'),
('ienumerable'),
('iqueryable'),
('linq'),
('deferred-execution'),
('entity-framework'),
('linq-filtering'),
('query-optimization'),
('sql-indexing'),
('sql-query-performance'),
('sql-execution-plans'),
('sql-joins'),
('sql-views'),
('database-abstraction'),
('read-models'),
('controller-service-pattern'),
('data-access-layer'),
('http-request-pipeline'),
('aspnet-core-middleware'),
('routing'),
('rest-api'),
('http-status-codes'),
('layered-architecture'),
('api-contracts'),
('input-validation'),
('request-tracing'),
('frontend-state'),
('browser-caching'),
('api-caching'),
('performance-profiling'),
('full-stack-development'),
('frontend-integration'),
('java-basics'),
('javascript'),
('javascript-fetch'),
('error-handling'),
('network-debugging'),
('duplicate-requests'),
('frontend-architecture'),
('api-client'),
('state-management'),
('spring-dependency-injection'),
('constructor-injection-java'),
('inversion-of-control'),
('testability'),
('java-streams'),
('stream-filter'),
('stream-map'),
('stream-collect'),
('stream-terminal-operations'),
('lazy-evaluation'),
('spring-boot'),
('controller-service-repository'),
('jpa'),
('repository-pattern'),
('hibernate'),
('hibernate-query-generation'),
('n-plus-one'),
('lazy-loading'),
('eager-loading'),
('orm'),
('persistence-api'),
('sql-transactions'),
('atomicity'),
('rollback'),
('data-consistency'),
('request-response-cycle'),
('database-access'),
('api-validation'),
('dto'),
('entity-exposure'),
('domain-model-separation'),
('data-projection'),
('full-stack-architecture'),
('sql-data-access'),
('performance-trade-offs'),
('maintainability');


-- =========================================================
-- INTERVIEWS
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description, difficulty, active, estimated_time_in_minutes)
SELECT
    'C# Frontend',
    id,
    1,
    'An interview covering C# frontend development, API integration, Blazor, authentication and frontend debugging.',
    'intermediate',
    TRUE,
    35
FROM interview_categories
WHERE name = 'Software Development';

SET @csharp_frontend_id = LAST_INSERT_ID();


INSERT INTO interviews
(interview_name, category_id, admin_id, description, difficulty, active, estimated_time_in_minutes)
SELECT
    'C# Backend',
    id,
    1,
    'An interview covering C# backend development, LINQ, Entity Framework, SQL and backend performance.',
    'intermediate',
    TRUE,
    40
FROM interview_categories
WHERE name = 'Software Development';

SET @csharp_backend_id = LAST_INSERT_ID();


INSERT INTO interviews
(interview_name, category_id, admin_id, description, difficulty, active, estimated_time_in_minutes)
SELECT
    'C# Full Stack',
    id,
    1,
    'An interview covering C# APIs, frontend integration, SQL, authentication and application architecture.',
    'intermediate',
    TRUE,
    40
FROM interview_categories
WHERE name = 'Software Development';

SET @csharp_fullstack_id = LAST_INSERT_ID();


INSERT INTO interviews
(interview_name, category_id, admin_id, description, difficulty, active, estimated_time_in_minutes)
SELECT
    'Java Frontend',
    id,
    1,
    'An interview covering frontend development, Java REST APIs, API contracts and frontend debugging.',
    'intermediate',
    TRUE,
    35
FROM interview_categories
WHERE name = 'Software Development';

SET @java_frontend_id = LAST_INSERT_ID();


INSERT INTO interviews
(interview_name, category_id, admin_id, description, difficulty, active, estimated_time_in_minutes)
SELECT
    'Java Backend',
    id,
    1,
    'An interview covering Java backend development, Spring Boot, JPA, Hibernate and SQL.',
    'intermediate',
    TRUE,
    40
FROM interview_categories
WHERE name = 'Software Development';

SET @java_backend_id = LAST_INSERT_ID();


INSERT INTO interviews
(interview_name, category_id, admin_id, description, difficulty, active, estimated_time_in_minutes)
SELECT
    'Java Full Stack',
    id,
    1,
    'An interview covering Java, Spring Boot, SQL, REST APIs and frontend integration.',
    'intermediate',
    TRUE,
    40
FROM interview_categories
WHERE name = 'Software Development';

SET @java_fullstack_id = LAST_INSERT_ID();


-- =========================================================
-- C# FRONTEND QUESTIONS
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_frontend_id,
    1,
    id,
    'easy',
    'Tell me about your experience working with C# and frontend applications.',
    'The candidate should briefly describe their experience with C# and frontend development, including the frameworks or types of applications they have worked on.'
FROM question_types
WHERE name = 'Introduction';


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_frontend_id,
    1,
    id,
    'easy',
    'What is dependency injection in C#, and why is it useful in a frontend application?',
    'Dependency injection means a class receives its dependencies from outside rather than creating them itself. In C#, this is commonly done through constructor injection and a DI container. It reduces coupling and makes components easier to test and replace.'
FROM question_types
WHERE name = 'Definition';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('dependency-injection', 'constructor-injection', 'loose-coupling');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_frontend_id,
    1,
    id,
    'medium',
    'Why should an HTTP request in a C# frontend normally use await instead of blocking on the result?',
    'Await allows the asynchronous HTTP operation to complete without synchronously blocking the calling thread. Blocking with Result or GetResult can reduce scalability and can cause deadlocks in some application environments.'
FROM question_types
WHERE name = 'Conceptual';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('async-await', 'http-client');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_frontend_id,
    1,
    id,
    'medium',
    'Implement a small C# method that asynchronously retrieves the name of a user from an HTTP API endpoint /users/{id}.',
    'The candidate should use an async method, await the HTTP request, check for an unsuccessful response where appropriate, and deserialize or extract the returned user name.'
FROM question_types
WHERE name = 'Implementation';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('async-await', 'http-client', 'rest-api', 'error-handling');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_frontend_id,
    1,
    id,
    'medium',
    CONCAT(
        'What is wrong with this C# code?',
        CHAR(10),
        CHAR(10),
        'var response = httpClient.GetAsync("/products").Result;',
        CHAR(10),
        CHAR(10),
        'How would you fix it?'
    ),
    'The code synchronously blocks while waiting for an asynchronous operation. The request should normally be awaited inside an async method.'
FROM question_types
WHERE name = 'Debugging';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('async-await', 'http-client');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_frontend_id,
    1,
    id,
    'medium',
    'How would you structure a Blazor application that consumes several backend APIs?',
    'A reasonable structure separates UI components from API clients and application logic. API communication should be handled by injected services or clients, while components focus primarily on presentation and user interaction.'
FROM question_types
WHERE name = 'Architecture';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('blazor-components', 'dependency-injection', 'api-client', 'loose-coupling');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_frontend_id,
    1,
    id,
    'medium',
    'Users report that navigating between Blazor pages sometimes causes the same API request to run multiple times. How would you investigate it?',
    'The candidate should inspect network requests and component lifecycle execution to identify where duplicate calls originate. They should check initialization logic, state changes, event handlers and whether a component is being recreated unexpectedly.'
FROM question_types
WHERE name = 'Debugging';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('component-lifecycle', 'network-debugging', 'duplicate-requests', 'frontend-state');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_frontend_id,
    1,
    id,
    'medium',
    'Tell me about a frontend performance problem you encountered and how you approached it.',
    'The candidate should describe a specific performance problem, how they investigated it, what the root cause was, and how they verified that their solution improved the application.'
FROM question_types
WHERE name = 'Experience';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name = 'performance-profiling';


-- =========================================================
-- C# BACKEND QUESTIONS
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_backend_id,
    1,
    id,
    'easy',
    'Tell me about your experience building backend applications with C#.',
    'The candidate should briefly describe their backend experience, the types of systems they have built, and the C# or .NET technologies they have used.'
FROM question_types
WHERE name = 'Introduction';


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_backend_id,
    1,
    id,
    'easy',
    'What is a SQL index?',
    'An index is a data structure that helps the database find rows faster without scanning the entire table. Indexes improve reads but require additional storage and can add overhead to inserts and updates.'
FROM question_types
WHERE name = 'Definition';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name = 'sql-indexing';


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_backend_id,
    1,
    id,
    'intermediate',
    'Why can filtering with IQueryable before ToList be more efficient than calling ToList first?',
    'Calling ToList first loads the query results into memory before filtering. Applying Where before ToList allows Entity Framework to translate the filter into SQL so the database returns only the required rows.'
FROM question_types
WHERE name = 'Conceptual';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('iqueryable', 'linq-filtering', 'entity-framework', 'deferred-execution');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_backend_id,
    1,
    id,
    'medium',
    'Implement a LINQ query that returns the names of active users whose age is at least 18.',
    'The candidate should filter users with IsActive and Age >= 18, then project the result to the Name property.'
FROM question_types
WHERE name = 'Implementation';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('linq', 'linq-filtering');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_backend_id,
    1,
    id,
    'medium',
    CONCAT(
        'What is wrong with this LINQ query?',
        CHAR(10),
        CHAR(10),
        'var users = db.Users.ToList().Where(u => u.IsActive);',
        CHAR(10),
        CHAR(10),
        'How would you fix it?'
    ),
    'ToList executes the database query before the filter is applied, causing all users to be loaded into memory. The Where filter should be applied before ToList so the database performs the filtering.'
FROM question_types
WHERE name = 'Debugging';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('linq-filtering', 'entity-framework', 'deferred-execution', 'query-optimization');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_backend_id,
    1,
    id,
    'hard',
    'How would you structure an ASP.NET Core API that needs to access SQL data and expose business operations?',
    'The API should separate HTTP handling, business logic and database access. Controllers should handle HTTP concerns, services should contain business logic, and a data-access layer should handle persistence. Dependencies should be injected rather than created directly.'
FROM question_types
WHERE name = 'Architecture';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('controller-service-pattern', 'data-access-layer', 'dependency-injection', 'layered-architecture');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_backend_id,
    1,
    id,
    'medium',
    'An API endpoint becomes slow after the amount of database data increases. How would you investigate it?',
    'The candidate should inspect the generated SQL and execution plan, then check indexes on filtering and joining columns. They should also look for expensive joins and unnecessarily large result sets.'
FROM question_types
WHERE name = 'Debugging';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('sql-query-performance', 'sql-execution-plans', 'sql-indexing', 'sql-joins', 'query-optimization');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_backend_id,
    1,
    id,
    'medium',
    'Tell me about a backend system where you had to make a trade-off between performance and maintainability.',
    'The candidate should describe the specific trade-off, why one approach was chosen, and what consequences the decision had for performance and maintainability.'
FROM question_types
WHERE name = 'Experience';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('performance-trade-offs', 'maintainability');


-- =========================================================
-- C# FULL STACK QUESTIONS
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_fullstack_id,
    1,
    id,
    'easy',
    'Tell me about your experience working across both the frontend and backend of a C# application.',
    'The candidate should briefly describe their full stack experience and their responsibilities across frontend, backend and database layers.'
FROM question_types
WHERE name = 'Introduction';


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_fullstack_id,
    1,
    id,
    'easy',
    'What is a REST API?',
    'A REST API is an HTTP-based interface where clients interact with resources through defined endpoints and HTTP methods such as GET, POST, PUT and DELETE. Responses commonly use formats such as JSON.'
FROM question_types
WHERE name = 'Definition';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name = 'rest-api';


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_fullstack_id,
    1,
    id,
    'intermediate',
    'Why should a frontend application use DTOs instead of directly displaying backend database entities?',
    'DTOs separate the API contract from the persistence model. They allow the backend to control which fields are exposed and prevent changes to database entities from automatically becoming API contract changes.'
FROM question_types
WHERE name = 'Conceptual';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('dto', 'api-contracts', 'entity-exposure', 'domain-model-separation');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_fullstack_id,
    1,
    id,
    'medium',
    'Implement a C# API endpoint that validates a required name field before creating a resource.',
    'The endpoint should validate that the name is present and valid before calling the service or persistence layer. Invalid input should result in an appropriate 4xx response rather than creating the resource.'
FROM question_types
WHERE name = 'Implementation';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('input-validation', 'api-validation', 'rest-api');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_fullstack_id,
    1,
    id,
    'medium',
    'Users report that changes made in the frontend are sometimes not visible immediately. How would you investigate this?',
    'The candidate should trace the update from the frontend through the API and database to verify that the new value is persisted. If persistence is correct, they should investigate frontend state, browser caching, API caching and stale objects.'
FROM question_types
WHERE name = 'Debugging';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('request-tracing', 'frontend-state', 'browser-caching', 'api-caching', 'data-consistency');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_fullstack_id,
    1,
    id,
    'hard',
    'How would you design a small full stack application with a C# API, SQL database and frontend?',
    'The application should separate frontend, API/business logic and database responsibilities. The API should expose a clear contract, validate input, use a dedicated data-access approach, and handle authentication at the appropriate boundary.'
FROM question_types
WHERE name = 'Architecture';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('layered-architecture', 'api-contracts', 'data-access-layer', 'input-validation', 'authentication');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_fullstack_id,
    1,
    id,
    'medium',
    'Would you choose browser caching or API caching for a read-heavy product catalogue? Explain the main trade-off.',
    'Browser caching can reduce network requests for data specific to a client, while API caching can reduce backend and database work for repeated requests across clients. The choice depends on data freshness, cache invalidation and whether the response is safe to share.'
FROM question_types
WHERE name = 'Trade-off';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('browser-caching', 'api-caching', 'performance-trade-offs');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @csharp_fullstack_id,
    1,
    id,
    'medium',
    'Describe a full stack feature you have built from the database through to the user interface.',
    'The candidate should describe a specific feature and explain their responsibilities across the database, backend/API and frontend. They should mention at least one technical decision or challenge.'
FROM question_types
WHERE name = 'Experience';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name = 'full-stack-development';


-- =========================================================
-- JAVA FRONTEND QUESTIONS
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_frontend_id,
    1,
    id,
    'easy',
    'Tell me about your experience working on frontend applications backed by Java services.',
    'The candidate should briefly describe their frontend experience and how they have interacted with Java-based backend services.'
FROM question_types
WHERE name = 'Introduction';


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_frontend_id,
    1,
    id,
    'easy',
    'What is a DTO and why is it commonly used between a frontend and backend?',
    'A DTO is an object used to transfer a defined set of data across a boundary such as an API. It prevents the API from needing to expose the internal persistence model directly.'
FROM question_types
WHERE name = 'Definition';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('dto', 'api-contracts', 'entity-exposure');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_frontend_id,
    1,
    id,
    'intermediate',
    'Why should a frontend check HTTP status codes instead of assuming that every fetch request succeeded?',
    'A fetch request can complete successfully at the network level while the server returns an HTTP error such as 400 or 500. The frontend should inspect the status and handle successful and unsuccessful responses differently.'
FROM question_types
WHERE name = 'Conceptual';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('javascript-fetch', 'http-status-codes', 'error-handling');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_frontend_id,
    1,
    id,
    'medium',
    'Implement the frontend logic needed to fetch /api/users and display the returned user list.',
    'The candidate should make the HTTP request, check for an unsuccessful response, parse the response body and store the resulting users in frontend state for rendering.'
FROM question_types
WHERE name = 'Implementation';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('javascript-fetch', 'api-client', 'frontend-state', 'error-handling');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_frontend_id,
    1,
    id,
    'medium',
    'A frontend application is making many more API requests than expected. How would you investigate the problem?',
    'The candidate should inspect browser network requests to identify duplicated calls and when they occur. They should then trace component lifecycle events, event handlers and state changes to determine what is triggering the additional requests.'
FROM question_types
WHERE name = 'Debugging';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('network-debugging', 'duplicate-requests', 'frontend-state', 'component-lifecycle');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_frontend_id,
    1,
    id,
    'medium',
    'How would you structure a frontend that consumes several Java backend services?',
    'API communication should be separated from UI components through dedicated API clients or services. The application should also have consistent approaches for shared state, authentication, error handling and API response models.'
FROM question_types
WHERE name = 'Architecture';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('frontend-architecture', 'api-client', 'state-management', 'error-handling', 'authentication');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_frontend_id,
    1,
    id,
    'medium',
    'Would you keep API calls directly inside UI components or use a dedicated API client layer? Explain the trade-off.',
    'A dedicated API client layer usually centralizes HTTP behavior and keeps UI components focused on presentation. Direct calls can be simpler for very small applications, but become harder to maintain as the number of endpoints grows.'
FROM question_types
WHERE name = 'Trade-off';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('api-client', 'frontend-architecture', 'loose-coupling', 'maintainability');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_frontend_id,
    1,
    id,
    'medium',
    'Tell me about a difficult frontend integration with a backend API and how you solved it.',
    'The candidate should explain the integration problem, how they investigated it, what the underlying issue was, and how they resolved it.'
FROM question_types
WHERE name = 'Experience';


-- =========================================================
-- JAVA BACKEND QUESTIONS
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_backend_id,
    1,
    id,
    'easy',
    'Tell me about your experience building backend applications with Java.',
    'The candidate should briefly describe their Java backend experience, the systems they worked on, and the main Java frameworks or technologies they used.'
FROM question_types
WHERE name = 'Introduction';


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_backend_id,
    1,
    id,
    'easy',
    'What is an ORM?',
    'An ORM maps objects in an application to records in a relational database. It allows developers to work with application objects while the ORM handles much of the SQL and persistence logic.'
FROM question_types
WHERE name = 'Definition';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name = 'orm';


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_backend_id,
    1,
    id,
    'intermediate',
    'Why does a Java Stream pipeline not execute simply because filter and map have been called?',
    'filter and map are intermediate operations. Streams are lazily evaluated and require a terminal operation such as collect, forEach or count before the pipeline is executed.'
FROM question_types
WHERE name = 'Conceptual';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('java-streams', 'stream-filter', 'stream-map', 'stream-terminal-operations', 'lazy-evaluation');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_backend_id,
    1,
    id,
    'medium',
    'Implement a Java Stream operation that returns the names of active users.',
    'The candidate should filter users using the active condition and map each remaining user to its name, followed by a terminal operation such as collect.'
FROM question_types
WHERE name = 'Implementation';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('java-streams', 'stream-filter', 'stream-map', 'stream-collect');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_backend_id,
    1,
    id,
    'medium',
    CONCAT(
        'What is wrong with this Java stream?',
        CHAR(10),
        CHAR(10),
        'users.stream()',
        CHAR(10),
        '    .filter(User::isActive)',
        CHAR(10),
        '    .map(User::getName);',
        CHAR(10),
        CHAR(10),
        'What is missing?'
    ),
    'The stream has no terminal operation, so the pipeline is not executed and no result is produced. A terminal operation such as collect is required.'
FROM question_types
WHERE name = 'Debugging';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('java-streams', 'stream-filter', 'stream-map', 'stream-terminal-operations', 'lazy-evaluation');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_backend_id,
    1,
    id,
    'hard',
    'How would you structure a Spring Boot application that exposes REST endpoints and stores data in SQL?',
    'The application should separate controllers, business/service logic and persistence. Controllers handle HTTP concerns, services contain business rules, and repositories handle database access through JPA, Hibernate, JDBC or another persistence mechanism.'
FROM question_types
WHERE name = 'Architecture';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('spring-boot', 'controller-service-repository', 'spring-dependency-injection', 'jpa', 'repository-pattern');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_backend_id,
    1,
    id,
    'medium',
    'A Spring Boot endpoint becomes slow when loading an entity with several relationships. What would you investigate?',
    'The candidate should inspect SQL generated by Hibernate and determine whether unnecessary queries are being executed. They should specifically consider N+1 queries, lazy versus eager loading, inefficient joins, large result sets and missing indexes.'
FROM question_types
WHERE name = 'Debugging';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('hibernate-query-generation', 'n-plus-one', 'lazy-loading', 'eager-loading', 'sql-indexing', 'sql-joins');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_backend_id,
    1,
    id,
    'medium',
    'When would you choose lazy loading over eager loading for a JPA relationship?',
    'Lazy loading is useful when related data is not always needed because it avoids loading it immediately. Eager loading can be appropriate when the related data is consistently required, but loading too much data can increase query cost.'
FROM question_types
WHERE name = 'Trade-off';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('lazy-loading', 'eager-loading', 'performance-trade-offs', 'hibernate-query-generation');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_backend_id,
    1,
    id,
    'medium',
    'Tell me about a Java backend performance problem you encountered and how you solved it.',
    'The candidate should describe the performance problem, how they measured or investigated it, the root cause, the change they made and how they verified the result.'
FROM question_types
WHERE name = 'Experience';


-- =========================================================
-- JAVA FULL STACK QUESTIONS
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_fullstack_id,
    1,
    id,
    'easy',
    'Tell me about your experience working across the frontend and Java backend of an application.',
    'The candidate should briefly describe their full stack experience and their responsibilities across frontend, backend and database layers.'
FROM question_types
WHERE name = 'Introduction';


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_fullstack_id,
    1,
    id,
    'easy',
    'What is a REST API?',
    'A REST API is an HTTP-based interface where clients interact with resources through defined endpoints and HTTP methods such as GET, POST, PUT and DELETE.'
FROM question_types
WHERE name = 'Definition';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name = 'rest-api';


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_fullstack_id,
    1,
    id,
    'intermediate',
    'Explain how a frontend, Spring Boot API and SQL database typically interact when loading a user profile.',
    'The frontend sends an HTTP request to a Spring Boot endpoint. The backend validates the request, loads the required data through its persistence layer, maps the result to an API response and returns it to the frontend.'
FROM question_types
WHERE name = 'Conceptual';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('request-response-cycle', 'spring-boot', 'database-access', 'dto');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_fullstack_id,
    1,
    id,
    'medium',
    'Implement the backend flow for creating a user: validate the request, call the service and return an appropriate HTTP response.',
    'The endpoint should validate the incoming DTO, call the service only when the request is valid, and return a successful response for creation or a 4xx response for invalid input.'
FROM question_types
WHERE name = 'Implementation';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('api-validation', 'input-validation', 'dto', 'rest-api');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_fullstack_id,
    1,
    id,
    'medium',
    CONCAT(
        'What is wrong with this Java code?',
        CHAR(10),
        CHAR(10),
        'users.stream()',
        CHAR(10),
        '    .map(User::getEmail);',
        CHAR(10),
        CHAR(10),
        'How would you fix it?'
    ),
    'The stream only defines an intermediate map operation and has no terminal operation, so no result is produced. A terminal operation such as collect is required.'
FROM question_types
WHERE name = 'Debugging';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('java-streams', 'stream-map', 'stream-terminal-operations', 'lazy-evaluation');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_fullstack_id,
    1,
    id,
    'hard',
    'How would you design a full stack application using Spring Boot, a SQL database and a JavaScript frontend?',
    'The frontend should communicate with the backend through a defined API contract. Spring Boot should separate controllers, business logic and persistence, while the database should be accessed through a dedicated data-access layer. Authentication and validation should be handled at appropriate boundaries.'
FROM question_types
WHERE name = 'Architecture';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('full-stack-architecture', 'spring-boot', 'rest-api', 'layered-architecture', 'sql-data-access', 'api-contracts');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_fullstack_id,
    1,
    id,
    'medium',
    'Would you return JPA entities directly from your REST API or map them to DTOs? Explain the trade-off.',
    'Returning entities is simpler for small applications but tightly couples the API to the persistence model. DTOs add mapping work but provide better control over the API contract and prevent internal entity changes from automatically affecting clients.'
FROM question_types
WHERE name = 'Trade-off';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name IN ('dto', 'entity-exposure', 'domain-model-separation', 'api-contracts', 'performance-trade-offs');


INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer)
SELECT
    @java_fullstack_id,
    1,
    id,
    'medium',
    'Describe a full stack feature you have implemented using Java and a frontend framework.',
    'The candidate should describe a specific feature and explain their contribution across the frontend, API/backend and persistence layers. They should mention the main technical decisions or challenges involved.'
FROM question_types
WHERE name = 'Experience';

SET @question_id = LAST_INSERT_ID();

INSERT INTO interview_question_concepts (question_id, concept_id)
SELECT @question_id, id
FROM concepts
WHERE name = 'full-stack-development';