INSERT INTO users (email,password,role) VALUES ('admin@example.com','$2y$10$\/0VAp1X3HiYiWX14lMCjZuqOzIkZY\/WtWemSM9ovx1ektl2.C4s3S','ADMIN');
INSERT INTO users (email,password,role) VALUES ('user@example.com','$2y$10$\/0VAp1X3HiYiWX14lMCjZuqOzIkZY\/WtWemSM9ovx1ektl2.C4s3S','USER');
INSERT INTO users (email,password,role) VALUES ('user2@example.com','$2y$10$\/0VAp1X3HiYiWX14lMCjZuqOzIkZY\/WtWemSM9ovx1ektl2.C4s3S','USER');

-- =========================================================
-- INTERVIEW CATEGORIES
-- =========================================================

INSERT INTO interview_categories (name)
VALUES ('Java'),('C#'),('Python'),('Cybersecurity'),('Data science');


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
('python-basics'),
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
('maintainability'),
('concurrency'),
('idempotency'),
('message-processing'),
('distributed-caching');


-- =========================================================
-- INTERVIEWS
-- =========================================================

-- =========================================================
-- JAVA EASY INTERVIEWS ONLY
-- Module 1 of 3: Easy
-- 2 interviews | 12 questions
-- Admin ID: 1
--
-- Compatible with the supplied database schema.
-- Medium and Hard will be separate scripts later.
-- =========================================================

-- =========================================================
-- 1. CREATE JAVA FUNDAMENTALS A
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Java Fundamentals A',
    c.id,
    1,
    'Java fundamentals, syntax and introductory technical knowledge.',
    'easy',
    TRUE,
    20
FROM interview_categories c
WHERE c.name = 'Java'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'Java Fundamentals A'
  );

-- =========================================================
-- 2. CREATE JAVA FUNDAMENTALS B
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Java Fundamentals B',
    c.id,
    1,
    'Java basics and object-oriented programming fundamentals.',
    'easy',
    TRUE,
    20
FROM interview_categories c
WHERE c.name = 'Java'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'Java Fundamentals B'
  );

-- =========================================================
-- 3. QUESTIONS FOR JAVA FUNDAMENTALS A
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'easy',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Tell me about yourself and your experience or learning journey with Java.' AS question_text,
        'A good answer introduces the candidate, describes their Java experience or learning, mentions relevant projects and explains their current development goals.' AS expected_answer

    UNION ALL

    SELECT
        'Definition',
        'What is Java, and what are its main characteristics?',
        'Java is a high-level, class-based programming language. Its characteristics include object-oriented programming, static typing, automatic memory management and execution through the Java Virtual Machine (JVM).'

    UNION ALL

    SELECT
        'Definition',
        'What is the difference between the JDK, JRE and JVM?',
        'The JVM executes Java bytecode. The JRE traditionally provides the JVM and runtime libraries. The JDK includes development tools such as the compiler. Modern Java distributions may package these components differently.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between a primitive type and a reference type in Java? Give examples.',
        'Primitive types include int, boolean and double. Reference variables refer to objects or arrays. Their assignment behaviour, default values and semantics differ.'

    UNION ALL

    SELECT
        'Implementation',
        'How would you write a Java method that determines whether an integer is even?',
        'A suitable solution is boolean isEven(int number) { return number % 2 == 0; }. The candidate should explain how the modulo operator determines whether a number is divisible by two.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between a class and an object in Java?',
        'A class defines a type, including its fields and methods. An object is an instance of that class created at runtime. Objects can hold their own instance state.'
) q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Java Fundamentals A'
  AND i.category_id = (
      SELECT id
      FROM interview_categories
      WHERE name = 'Java'
      LIMIT 1
  )
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

-- =========================================================
-- 4. QUESTIONS FOR JAVA FUNDAMENTALS B
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'easy',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Describe a small Java program or project you have worked on, or explain one you would like to build.' AS question_text,
        'A good answer explains the project goal, intended functionality, Java features involved and the candidate’s own contribution or proposed implementation.' AS expected_answer

    UNION ALL

    SELECT
        'Definition',
        'What is encapsulation in object-oriented programming?',
        'Encapsulation combines state and behaviour within a class and controls access to internal state using access modifiers and appropriate methods. It helps protect object invariants.'

    UNION ALL

    SELECT
        'Definition',
        'What is inheritance in Java, and when might you use it?',
        'Inheritance allows a class to extend another class and reuse or specialise its behaviour. It is useful for a genuine is-a relationship, while composition may be better for other relationships.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between == and equals() when comparing Java objects?',
        'For reference types, == compares object identity. equals() compares logical equality according to the implementation. Object.equals() uses identity semantics unless overridden.'

    UNION ALL

    SELECT
        'Implementation',
        'How would you create a Java class representing a Book with a title and an author?',
        'A suitable solution defines a Book class with private String fields, a constructor and appropriate accessors if needed. The candidate should explain encapsulation and initialisation.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between a method parameter and a return value?',
        'Parameters provide input to a method. A return value communicates a result to the caller. A void method does not return a value.'
) q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Java Fundamentals B'
  AND i.category_id = (
      SELECT id
      FROM interview_categories
      WHERE name = 'Java'
      LIMIT 1
  )
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

  -- =========================================================
-- 1. CREATE THE MEDIUM INTERVIEW IF IT DOES NOT EXIST
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Java Application Development A',
    c.id,
    1,
    'Java application development, collections, exception handling and practical debugging.',
    'medium',
    TRUE,
    30
FROM interview_categories c
WHERE c.name = 'Java'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'Java Application Development A'
        AND existing.category_id = c.id
  );

-- =========================================================
-- 2. INSERT THE SEVEN QUESTIONS
-- Existing questions are skipped if this script is rerun.
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'medium',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    -- QUESTION 1: INTRODUCTION
    SELECT
        'Introduction' AS question_type,
        'Introduce yourself as a Java developer and explain which areas of Java you are most comfortable working with.' AS question_text,
        'A good answer describes relevant experience, Java skills, application types, responsibilities and areas for further development.' AS expected_answer

    UNION ALL

    -- QUESTION 2: EXPERIENCE
    SELECT
        'Experience',
        'Describe a Java task or project that was challenging. What was your responsibility, and how did you approach it?',
        'A good answer explains the context, individual contribution, technical decisions, implementation, outcome and lessons learned. A substantial learning project is acceptable.'

    UNION ALL

    -- QUESTION 3: DEFINITION
    SELECT
        'Definition',
        'What is the Java Collections Framework, and why is it useful?',
        'It provides interfaces and implementations for groups of objects, including List, Set, Queue and Map. Collection choice affects ordering, uniqueness, lookup performance and supported operations.'

    UNION ALL

    -- QUESTION 4: TECHNICAL
    SELECT
        'Conceptual',
        'What is the difference between filter() and map() in the Java Stream API?',
        'filter() retains elements that satisfy a predicate. map() transforms elements into other values. Both are intermediate operations that are generally evaluated when a terminal operation consumes the stream.'

    UNION ALL

    -- QUESTION 5: TECHNICAL
    SELECT
        'Implementation',
        'How would you handle an operation that may throw an IOException when reading a file?',
        'Use an appropriate try-catch block or declare the exception using throws. Try-with-resources is suitable for closeable resources. Errors should not be silently swallowed, and useful diagnostic information should be preserved.'

    UNION ALL

    -- QUESTION 6: PROBLEM SOLVING
    SELECT
        'Debugging',
        'A Java service occasionally fails with a NullPointerException. How would you investigate and fix the problem?',
        'Inspect the stack trace, reproduce the issue, identify the failing dereference and determine why the value is null. Fix the underlying cause and add suitable validation or regression tests.'

    UNION ALL

    -- QUESTION 7: PROBLEM SOLVING
    SELECT
        'Debugging',
        'A method works for most inputs but fails when given an empty list. How would you diagnose and prevent this?',
        'Inspect assumptions about list size, identify operations requiring elements, define the expected empty-input behaviour, implement appropriate handling and add regression tests.'
) q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Java Application Development A'
  AND i.category_id = (
      SELECT id
      FROM interview_categories
      WHERE name = 'Java'
      LIMIT 1
  )
  AND i.difficulty = 'medium'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

-- =========================================================
-- JAVA MEDIUM INTERVIEW B
-- Module 2B: Object-Oriented Design and Debugging
-- 1 interview | 7 questions
-- Admin ID: 1
-- =========================================================

START TRANSACTION;

-- =========================================================
-- 1. CREATE INTERVIEW IF IT DOES NOT EXIST
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Java Application Development B',
    c.id,
    1,
    'Java application development, object-oriented design, collections and debugging.',
    'medium',
    TRUE,
    30
FROM interview_categories c
WHERE c.name = 'Java'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'Java Application Development B'
        AND existing.category_id = c.id
  );

-- =========================================================
-- 2. INSERT THE SEVEN QUESTIONS
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'medium',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    -- QUESTION 1: INTRODUCTION
    SELECT
        'Introduction' AS question_type,
        'Walk me through your understanding of how a Java application is organised, from its classes to its execution.' AS question_text,
        'A good answer explains classes, objects, methods, entry points and compilation or runtime execution. More experienced candidates may also discuss packages and application layers.' AS expected_answer

    UNION ALL

    -- QUESTION 2: EXPERIENCE
    SELECT
        'Experience',
        'Tell me about a time you had to change existing Java code without breaking its current behaviour. How did you approach it?',
        'A good answer explains how the candidate understood the existing implementation, identified dependencies, used tests, made controlled changes and validated the outcome.'

    UNION ALL

    -- QUESTION 3: DEFINITION
    SELECT
        'Definition',
        'What is polymorphism in Java, and how can it make an application easier to extend?',
        'Polymorphism allows code to use a common supertype or interface while different implementations provide different behaviour. It supports substitutability and reduces dependence on concrete implementations.'

    UNION ALL

    -- QUESTION 4: TECHNICAL
    SELECT
        'Conceptual',
        'What is the difference between an interface and an abstract class in Java?',
        'An interface defines a contract and can contain abstract, default and static methods. An abstract class can share instance state, constructors and implementation. A class can implement multiple interfaces but extend only one class.'

    UNION ALL

    -- QUESTION 5: TECHNICAL
    SELECT
        'Implementation',
        'You need to count how many times each word occurs in a list. Which Java collection would you use, and how would you implement it?',
        'Use a Map<String, Integer>. Iterate through the words and update counts with merge(), compute() or an equivalent approach. Normalise words first if case-insensitive counting is required.'

    UNION ALL

    -- QUESTION 6: PROBLEM SOLVING
    SELECT
        'Debugging',
        'A Java method unexpectedly changes an object that another part of the application also uses. How would you find and prevent unintended changes?',
        'Trace shared references and mutations, identify methods changing object state and reproduce the issue. Depending on requirements, use defensive copies, immutable objects or clearer ownership boundaries.'

    UNION ALL

    -- QUESTION 7: PROBLEM SOLVING
    SELECT
        'Trade-off',
        'You can solve a requirement using inheritance or composition. How would you choose between them?',
        'Use inheritance when a genuine subtype relationship exists and substitutability is preserved. Composition is often preferable when behaviour should be combined or delegated flexibly. Consider coupling, testability and maintainability.'
) q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Java Application Development B'
  AND i.category_id = (
      SELECT id
      FROM interview_categories
      WHERE name = 'Java'
      LIMIT 1
  )
  AND i.difficulty = 'medium'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );


COMMIT;

-- =========================================================
-- JAVA HARD INTERVIEW A
-- Module 3A: Advanced Java Engineering
-- 1 interview | 8 questions
-- Admin ID: 1
-- =========================================================

START TRANSACTION;

-- =========================================================
-- 1. CREATE THE INTERVIEW IF IT DOES NOT EXIST
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Java Advanced Engineering A',
    c.id,
    1,
    'Advanced Java engineering, concurrency, memory management, performance diagnostics and system reliability.',
    'hard',
    TRUE,
    40
FROM interview_categories c
WHERE c.name = 'Java'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'Java Advanced Engineering A'
        AND existing.category_id = c.id
  );

-- =========================================================
-- 2. INSERT THE EIGHT QUESTIONS
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'hard',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    -- QUESTION 1: INTRODUCTION
    SELECT
        'Introduction' AS question_type,
        'Summarise your Java engineering experience, focusing on the most technically demanding systems or features you have worked on.' AS question_text,
        'A strong answer describes relevant projects, responsibilities, system complexity, technical decisions and outcomes. The candidate should distinguish their individual contributions from the work of the wider team.' AS expected_answer

    UNION ALL

    -- QUESTION 2: EXPERIENCE
    SELECT
        'Experience',
        'Describe a significant Java issue you investigated. How did you identify the root cause, mitigate the impact and verify the fix?',
        'A strong answer explains evidence gathering, reproduction, root-cause analysis, risk-controlled mitigation, regression prevention and verification. A substantial non-production project is acceptable when production experience is unavailable.'

    UNION ALL

    -- QUESTION 3: TECHNICAL
    SELECT
        'Conceptual',
        'Explain how the Java Memory Model affects visibility and ordering when multiple threads access shared state.',
        'The Java Memory Model defines legal interactions between threads, including happens-before relationships. Synchronisation, volatile fields and other concurrency primitives can establish visibility and ordering guarantees. Unsynchronised conflicting accesses can create data races.'

    UNION ALL

    -- QUESTION 4: TECHNICAL
    SELECT
        'Conceptual',
        'What is the difference between synchronized and volatile in Java, and when is each appropriate?',
        'synchronized provides mutual exclusion and memory-visibility guarantees. volatile provides visibility and ordering guarantees for a variable but does not make compound operations such as count++ atomic. The correct choice depends on the coordination required.'

    UNION ALL

    -- QUESTION 5: TECHNICAL
    SELECT
        'Architecture',
        'How would you design a Java service that performs concurrent tasks while preventing uncontrolled resource consumption?',
        'Use bounded executors or thread pools, explicit concurrency limits, suitable queues, timeouts and defined rejection behaviour. Consider cancellation, graceful shutdown, backpressure, observability and whether tasks are CPU-bound or I/O-bound.'

    UNION ALL

    -- QUESTION 6: PROBLEM SOLVING
    SELECT
        'Debugging',
        'A Java application becomes progressively slower and eventually runs out of memory. How would you distinguish a memory leak from excessive allocation or another issue?',
        'Gather heap and garbage-collection evidence, inspect memory trends and heap dumps, examine retained objects and allocation profiles, and correlate findings with workload. Identify the cause before changing memory settings or application code.'

    UNION ALL

    -- QUESTION 7: PROBLEM SOLVING
    SELECT
        'Trade-off',
        'A service needs higher throughput, but increasing the thread count causes more contention and worse response times. How would you evaluate the available options?',
        'Measure throughput, latency percentiles, CPU utilisation, blocking and contention. Identify the bottleneck before changing concurrency. Consider bounded concurrency, reduced shared state, batching or asynchronous I/O, and validate changes with repeatable benchmarks.'

    UNION ALL

    -- QUESTION 8: PROBLEM SOLVING
    SELECT
        'Debugging',
        'Two Java threads occasionally become unresponsive after a new feature is deployed. Explain how you would investigate whether a deadlock or another concurrency problem is responsible.',
        'Capture thread dumps during the incident, inspect thread states and lock ownership, and look for circular lock dependencies. If no deadlock is present, investigate starvation, lock contention, blocked I/O and executor exhaustion. Reproduce the issue where possible, fix the underlying cause and add concurrency-focused tests or monitoring.'
) q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Java Advanced Engineering A'
  AND i.category_id = (
      SELECT id
      FROM interview_categories
      WHERE name = 'Java'
      LIMIT 1
  )
  AND i.difficulty = 'hard'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

COMMIT;

-- =========================================================
-- C# EASY INTERVIEWS
-- Module 1: Easy
-- 2 interviews | 12 questions
-- Admin ID: 1
-- =========================================================

START TRANSACTION;

-- =========================================================
-- 1. CREATE C# FUNDAMENTALS A
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'C# Fundamentals A',
    c.id,
    1,
    'C# fundamentals, .NET basics, syntax and introductory programming concepts.',
    'easy',
    TRUE,
    20
FROM interview_categories c
WHERE c.name = 'C#'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'C# Fundamentals A'
        AND existing.category_id = c.id
  );

-- =========================================================
-- 2. CREATE C# FUNDAMENTALS B
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'C# Fundamentals B',
    c.id,
    1,
    'C# object-oriented programming, methods, types and fundamental language concepts.',
    'easy',
    TRUE,
    20
FROM interview_categories c
WHERE c.name = 'C#'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'C# Fundamentals B'
        AND existing.category_id = c.id
  );

-- =========================================================
-- 3. QUESTIONS FOR C# FUNDAMENTALS A
-- 1 introduction, 2 definitions, 3 technical
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'easy',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Tell me about yourself and your experience or learning journey with C#.' AS question_text,
        'A good answer introduces the candidate, describes their C# experience or learning, mentions relevant projects and explains their current development goals.' AS expected_answer

    UNION ALL

    SELECT
        'Definition',
        'What is C#, and what is it commonly used for?',
        'C# is a statically typed, general-purpose programming language commonly used with .NET to build web applications, APIs, desktop software, cloud services, games and other applications.'

    UNION ALL

    SELECT
        'Definition',
        'What is the difference between C# and .NET?',
        'C# is a programming language. .NET is a development platform that provides a runtime, libraries and tools for building and running applications written in C# and other supported languages.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between a value type and a reference type in C#? Give examples.',
        'Value types, such as int, bool and structs, hold their values directly. Reference-type variables refer to objects, such as class instances and arrays. Assignment and copying behaviour differ, and value types can also be fields of reference types.'

    UNION ALL

    SELECT
        'Implementation',
        'How would you write a C# method that determines whether an integer is even?',
        'A suitable solution is bool IsEven(int number) { return number % 2 == 0; }. The candidate should explain how the modulo operator checks divisibility by two.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between a class and an object in C#?',
        'A class defines a type, including its fields, properties and methods. An object is an instance of a class created at runtime and can hold its own instance state.'
) q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'C# Fundamentals A'
  AND i.category_id = (
      SELECT id
      FROM interview_categories
      WHERE name = 'C#'
      LIMIT 1
  )
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

-- =========================================================
-- 4. QUESTIONS FOR C# FUNDAMENTALS B
-- 1 introduction, 2 definitions, 3 technical
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'easy',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Describe a small C# program or project you have worked on, or explain one you would like to build.' AS question_text,
        'A good answer explains the project goal, intended functionality, C# features involved and the candidate’s own contribution or proposed implementation.' AS expected_answer

    UNION ALL

    SELECT
        'Definition',
        'What is encapsulation in object-oriented programming?',
        'Encapsulation combines data and behaviour within a class and controls access to internal state using access modifiers and suitable properties or methods. It helps protect valid object state.'

    UNION ALL

    SELECT
        'Definition',
        'What is inheritance in C#, and when might you use it?',
        'Inheritance allows a class to derive from another class and reuse or specialise its behaviour. It is useful when a genuine is-a relationship exists. C# classes support single class inheritance but can implement multiple interfaces.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between a field and a property in C#?',
        'A field stores data directly. A property provides controlled access through get and set or init accessors and can include validation or computed behaviour. Properties are commonly used for a class public API.'

    UNION ALL

    SELECT
        'Implementation',
        'How would you create a C# class representing a Book with a title and an author?',
        'A suitable solution defines a Book class with private fields or public properties, a constructor if appropriate and suitable initialisation. The candidate should explain how the design protects and exposes the object state.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between a method parameter and a return value in C#?',
        'Parameters provide input to a method. A return value communicates a result to the caller. A void method does not return a value, while other methods declare a return type.'
) q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'C# Fundamentals B'
  AND i.category_id = (
      SELECT id
      FROM interview_categories
      WHERE name = 'C#'
      LIMIT 1
  )
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

  COMMIT;

  -- =========================================================
-- C# MEDIUM INTERVIEWS
-- 2 interviews | 14 questions
-- Admin ID: 1
-- =========================================================

START TRANSACTION;

-- =========================================================
-- 1. CREATE INTERVIEW A
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'C# Application Development A',
    c.id,
    1,
    'C# collections, LINQ, exception handling and debugging.',
    'medium',
    TRUE,
    30
FROM interview_categories c
WHERE c.name = 'C#'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews i
      WHERE i.interview_name = 'C# Application Development A'
        AND i.category_id = c.id
  );

-- =========================================================
-- 2. CREATE INTERVIEW B
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'C# Application Development B',
    c.id,
    1,
    'C# object-oriented design, dependency injection and debugging.',
    'medium',
    TRUE,
    30
FROM interview_categories c
WHERE c.name = 'C#'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews i
      WHERE i.interview_name = 'C# Application Development B'
        AND i.category_id = c.id
  );

-- =========================================================
-- 3. QUESTIONS FOR INTERVIEW A
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'medium',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Introduce yourself as a C# developer and explain which areas of C# and .NET you are most comfortable working with.' AS question_text,
        'A good answer describes relevant experience, C# skills, application types, responsibilities and areas for further development.' AS expected_answer

    UNION ALL

    SELECT
        'Experience',
        'Describe a C# project or task that presented a technical challenge. What was your responsibility, and how did you approach it?',
        'A good answer explains the context, individual contribution, technical decisions, implementation, outcome and lessons learned. A substantial learning project is acceptable.'

    UNION ALL

    SELECT
        'Definition',
        'What is LINQ in C#, and why is it useful?',
        'LINQ means Language Integrated Query. It provides a consistent way to query and transform data from collections and other supported providers. Common operations include filtering, projection, sorting and grouping.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between IEnumerable<T> and IQueryable<T>?',
        'IEnumerable<T> represents an enumerable sequence, commonly processed in application memory. IQueryable<T> represents a query through an expression tree that a provider may translate for execution elsewhere, such as in a database. Execution behaviour depends on the provider.'

    UNION ALL

    SELECT
        'Implementation',
        'How would you use LINQ to retrieve all products costing more than 100 and order them from the lowest price to the highest?',
        'For example: var result = products.Where(p => p.Price > 100).OrderBy(p => p.Price); Where filters the products and OrderBy sorts them in ascending price order.'

    UNION ALL

    SELECT
        'Debugging',
        'A C# application throws an exception when processing a particular record, but other records work correctly. How would you investigate and resolve the issue?',
        'Reproduce the failure, inspect the exception type, message and stack trace, examine the failing record and identify the underlying cause. Correct the problem, handle expected exceptions appropriately and add regression tests.'

    UNION ALL

    SELECT
        'Debugging',
        'A LINQ query returns an empty collection even though matching records appear to exist. How would you troubleshoot the problem?',
        'Inspect the source data, filter predicates, null values, string casing and data types. For remote query providers, inspect the generated query and parameters. Reproduce the issue with representative data and add tests.'
) AS q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'C# Application Development A'
  AND i.category_id = (
      SELECT id
      FROM interview_categories
      WHERE name = 'C#'
      LIMIT 1
  )
  AND i.difficulty = 'medium'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

-- =========================================================
-- 4. QUESTIONS FOR INTERVIEW B
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'medium',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Explain how you would structure a maintainable C# application, from its core logic to its external dependencies.' AS question_text,
        'A good answer explains separation of responsibilities, suitable class and interface boundaries, dependency management and how the structure supports testing and future changes.' AS expected_answer

    UNION ALL

    SELECT
        'Experience',
        'Tell me about a time you improved existing C# code to make it easier to maintain or test. What did you change, and what was the result?',
        'A good answer identifies the original problem, the candidate contribution, the changes made and the outcome. Strong answers explain how existing behaviour was preserved and how improvements were verified.'

    UNION ALL

    SELECT
        'Definition',
        'What is dependency injection in .NET, and what problems does it help solve?',
        'Dependency injection supplies a class with the dependencies it needs instead of requiring it to construct them directly. It reduces tight coupling, supports substituting implementations and improves testability. .NET provides a built-in dependency injection container.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between an interface and an abstract class in C#?',
        'An interface defines a contract that types can implement. An abstract class can share implementation and instance state. A class can implement multiple interfaces but inherit from only one class. The choice depends on the relationship and design requirements.'

    UNION ALL

    SELECT
        'Implementation',
        'How would you design a service that retrieves customer information from a data source while keeping the business logic easy to unit test?',
        'Define an abstraction for the data access dependency and inject it through the service constructor. Unit tests can supply a fake or mock implementation. Integration tests should separately verify the real data access implementation.'

    UNION ALL

    SELECT
        'Debugging',
        'A C# service works locally but fails when deployed because one of its dependencies cannot be resolved. How would you diagnose the issue?',
        'Inspect the exception and logs, verify dependency registrations, constructor requirements, configuration and service lifetimes. Check environment-specific setup, correct the registration or configuration and verify the fix with tests.'

    UNION ALL

    SELECT
        'Trade-off',
        'You can implement a feature directly inside an existing class or introduce a separate service and interface. How would you decide which approach is appropriate?',
        'Consider responsibility boundaries, complexity, reuse, testability, expected change and maintenance cost. Introduce a separate abstraction when it creates a meaningful boundary, but avoid unnecessary layers that increase complexity.'
) AS q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'C# Application Development B'
  AND i.category_id = (
      SELECT id
      FROM interview_categories
      WHERE name = 'C#'
      LIMIT 1
  )
  AND i.difficulty = 'medium'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

COMMIT;


-- =========================================================
-- C# HARD INTERVIEW
-- Module 3: Hard A
-- 1 interview | 8 questions
-- Admin ID: 1
-- =========================================================

START TRANSACTION;

-- =========================================================
-- 1. CREATE THE INTERVIEW IF IT DOES NOT EXIST
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'C# Advanced Engineering A',
    c.id,
    1,
    'Advanced C# engineering, asynchronous programming, concurrency, performance and system reliability.',
    'hard',
    TRUE,
    40
FROM interview_categories c
WHERE c.name = 'C#'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'C# Advanced Engineering A'
        AND existing.category_id = c.id
  );

-- =========================================================
-- 2. INSERT THE EIGHT QUESTIONS
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'hard',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    -- QUESTION 1: INTRODUCTION
    SELECT
        'Introduction' AS question_type,
        'Summarise your C# engineering experience, focusing on the most technically demanding systems or features you have worked on.' AS question_text,
        'A strong answer describes relevant projects, responsibilities, system complexity, technical decisions and outcomes. The candidate should distinguish their individual contribution from the work of the wider team.' AS expected_answer

    UNION ALL

    -- QUESTION 2: EXPERIENCE
    SELECT
        'Experience',
        'Describe a significant C# or .NET issue you investigated. How did you identify the root cause, reduce the impact and verify the fix?',
        'A strong answer explains evidence gathering, reproduction, root cause analysis, risk-controlled mitigation, regression prevention and verification. A substantial non-production project is acceptable when production experience is unavailable.'

    UNION ALL

    -- QUESTION 3: TECHNICAL
    SELECT
        'Conceptual',
        'Explain how async and await work in C# and why asynchronous code does not necessarily require a new thread.',
        'The async and await keywords simplify asynchronous control flow. An asynchronous operation can yield while waiting for I/O and resume when the operation completes. It does not inherently create a new thread. CPU-bound work may require explicit scheduling or parallelism.'

    UNION ALL

    -- QUESTION 4: TECHNICAL
    SELECT
        'Conceptual',
        'What is the difference between Task.WhenAll and Parallel.ForEach in .NET, and when would you use each?',
        'Task.WhenAll waits for multiple tasks to complete and is commonly used to coordinate asynchronous operations. Parallel.ForEach executes iterations concurrently and is useful for suitable parallel workloads. The choice depends on whether the work is asynchronous I/O or CPU-bound processing, as well as resource limits and thread safety.'

    UNION ALL

    -- QUESTION 5: TECHNICAL
    SELECT
        'Architecture',
        'How would you design a high-throughput .NET API that needs to handle many concurrent requests without exhausting database connections or other resources?',
        'Use asynchronous I/O, connection pooling, bounded concurrency where appropriate, timeouts, cancellation and suitable resource limits. Optimise database queries, monitor latency and resource usage, and apply backpressure or rate limiting when necessary. Validate the design with load tests.'

    UNION ALL

    -- QUESTION 6: PROBLEM SOLVING
    SELECT
        'Debugging',
        'A .NET application gradually consumes more memory and eventually becomes unstable. How would you determine whether the cause is a memory leak, excessive allocation or another problem?',
        'Collect memory and garbage collection metrics, compare memory snapshots and inspect heap dumps or allocation profiles. Identify objects that remain reachable unexpectedly, large allocation rates or excessive retention. Fix the underlying cause and verify the result under representative workloads.'

    UNION ALL

    -- QUESTION 7: PROBLEM SOLVING
    SELECT
        'Trade-off',
        'An API becomes slower after a developer introduces parallel processing to improve throughput. How would you investigate the regression and decide whether parallelism is appropriate?',
        'Measure throughput, response time percentiles, CPU usage, contention, database load and thread pool behaviour. Identify the actual bottleneck before changing the design. Parallelism can increase contention and resource consumption. Compare alternatives using repeatable benchmarks and realistic workloads.'

    UNION ALL

    -- QUESTION 8: PROBLEM SOLVING
    SELECT
        'Debugging',
        'A .NET service occasionally processes the same request twice, resulting in duplicate records. How would you identify the cause and make the operation reliable?',
        'Trace request identifiers and logs to determine whether retries, duplicate client requests or concurrent processing caused the issue. Use appropriate idempotency keys, database uniqueness constraints and transactions where needed. Consider retry policies and failure boundaries, then add tests for repeated and concurrent requests.'
) AS q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'C# Advanced Engineering A'
  AND i.category_id = (
      SELECT id
      FROM interview_categories
      WHERE name = 'C#'
      LIMIT 1
  )
  AND i.difficulty = 'hard'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

COMMIT;





-- =========================================================
-- CYBERSECURITY CONCEPTS
-- Initial cybersecurity concept library
-- Safe to rerun: existing names are skipped
-- =========================================================

INSERT INTO concepts (name)
SELECT new_concepts.name
FROM (
    SELECT 'cybersecurity-basics' AS name
    UNION ALL SELECT 'cia-triad'
    UNION ALL SELECT 'security-principles'
    UNION ALL SELECT 'threat-modeling'
    UNION ALL SELECT 'attack-surface'
    UNION ALL SELECT 'threat-actors'
    UNION ALL SELECT 'security-vulnerabilities'
    UNION ALL SELECT 'security-risk-assessment'
    UNION ALL SELECT 'defense-in-depth'
    UNION ALL SELECT 'least-privilege'

    UNION ALL SELECT 'network-security'
    UNION ALL SELECT 'tcp-ip'
    UNION ALL SELECT 'dns-security'
    UNION ALL SELECT 'firewall-rules'
    UNION ALL SELECT 'network-segmentation'
    UNION ALL SELECT 'vpn'
    UNION ALL SELECT 'tls-https'
    UNION ALL SELECT 'public-key-cryptography'
    UNION ALL SELECT 'symmetric-encryption'
    UNION ALL SELECT 'hashing'
    UNION ALL SELECT 'digital-signatures'
    UNION ALL SELECT 'certificate-management'

    UNION ALL SELECT 'owasp-top-ten'
    UNION ALL SELECT 'sql-injection'
    UNION ALL SELECT 'cross-site-scripting'
    UNION ALL SELECT 'cross-site-request-forgery'
    UNION ALL SELECT 'server-side-request-forgery'
    UNION ALL SELECT 'secure-coding'
    UNION ALL SELECT 'output-encoding'
    UNION ALL SELECT 'security-headers'
    UNION ALL SELECT 'secure-session-management'
    UNION ALL SELECT 'secrets-management'

    UNION ALL SELECT 'multi-factor-authentication'
    UNION ALL SELECT 'identity-management'
    UNION ALL SELECT 'role-based-access-control'
    UNION ALL SELECT 'privileged-access-management'
    UNION ALL SELECT 'password-security'
    UNION ALL SELECT 'oauth2'
    UNION ALL SELECT 'openid-connect'
    UNION ALL SELECT 'session-security'

    UNION ALL SELECT 'security-logging'
    UNION ALL SELECT 'security-monitoring'
    UNION ALL SELECT 'siem'
    UNION ALL SELECT 'intrusion-detection'
    UNION ALL SELECT 'endpoint-detection-response'
    UNION ALL SELECT 'vulnerability-scanning'
    UNION ALL SELECT 'patch-management'
    UNION ALL SELECT 'incident-response'
    UNION ALL SELECT 'digital-forensics'
    UNION ALL SELECT 'malware-analysis'

    UNION ALL SELECT 'secure-software-development-lifecycle'
    UNION ALL SELECT 'penetration-testing'
    UNION ALL SELECT 'risk-mitigation'
    UNION ALL SELECT 'security-auditing'
    UNION ALL SELECT 'backup-and-recovery'
    UNION ALL SELECT 'business-continuity'
    UNION ALL SELECT 'data-classification'
    UNION ALL SELECT 'security-compliance'
) AS new_concepts
WHERE NOT EXISTS (
    SELECT 1
    FROM concepts existing
    WHERE existing.name = new_concepts.name
);

-- =========================================================
-- CYBERSECURITY EASY INTERVIEWS
-- 2 interviews | 12 questions
-- Admin ID: 1
-- ASCII-SAFE VERSION
-- =========================================================

START TRANSACTION;

-- =========================================================
-- 1. CREATE CYBERSECURITY FUNDAMENTALS A
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Cybersecurity Fundamentals A',
    c.id,
    1,
    'Security principles, threats, vulnerabilities and risk awareness.',
    'easy',
    TRUE,
    20
FROM interview_categories c
WHERE c.name = 'Cybersecurity'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'Cybersecurity Fundamentals A'
        AND existing.category_id = c.id
  );

-- =========================================================
-- 2. CREATE CYBERSECURITY FUNDAMENTALS B
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Cybersecurity Fundamentals B',
    c.id,
    1,
    'Network security, authentication, access control and safe computing.',
    'easy',
    TRUE,
    20
FROM interview_categories c
WHERE c.name = 'Cybersecurity'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'Cybersecurity Fundamentals B'
        AND existing.category_id = c.id
  );

-- =========================================================
-- 3. QUESTIONS FOR FUNDAMENTALS A
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'easy',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Introduce yourself and explain why cybersecurity interests you.' AS question_text,
        'A good answer describes the candidate interest in protecting systems and data, relevant learning or experience, and areas of cybersecurity they want to explore.' AS expected_answer

    UNION ALL

    SELECT
        'Definition',
        'What is cybersecurity and what is its main purpose?',
        'Cybersecurity protects systems, networks, applications and data against unauthorized access, disruption, damage and theft. Its purpose is to reduce security risks and protect confidentiality, integrity and availability.'

    UNION ALL

    SELECT
        'Definition',
        'What is the CIA triad in cybersecurity?',
        'The CIA triad stands for confidentiality, integrity and availability. Confidentiality restricts access to authorized parties. Integrity protects information against improper alteration. Availability ensures systems and data remain accessible when needed.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between a threat, a vulnerability and a risk?',
        'A threat is something capable of causing harm. A vulnerability is a weakness that could be exploited. Risk describes the potential impact and likelihood of harm when a threat exploits a vulnerability.'

    UNION ALL

    SELECT
        'Conceptual',
        'Why is the principle of least privilege important when managing user accounts?',
        'Least privilege gives users and services only the permissions required for their tasks. It reduces the damage caused by compromised accounts, mistakes or misuse of permissions.'

    UNION ALL

    SELECT
        'Implementation',
        'An employee receives an unexpected email asking them to open an attachment and enter their company password. What should they do?',
        'They should not open the attachment or enter their password. They should verify the request through a trusted channel, report the suspicious message and follow security team instructions. If they already entered their credentials, they should report the incident immediately.'
) AS q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Cybersecurity Fundamentals A'
  AND i.category_id = (
      SELECT id
      FROM interview_categories
      WHERE name = 'Cybersecurity'
      LIMIT 1
  )
  AND i.difficulty = 'easy'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

-- =========================================================
-- 4. QUESTIONS FOR FUNDAMENTALS B
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'easy',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    SELECT
        'Introduction' AS question_type,
        'What cybersecurity practices do you follow when using a computer, browsing the internet or accessing online accounts?' AS question_text,
        'A good answer mentions unique passwords, multifactor authentication, regular updates, checking links carefully, using trusted networks and reporting suspicious activity.' AS expected_answer

    UNION ALL

    SELECT
        'Definition',
        'What is the difference between authentication and authorization?',
        'Authentication verifies the identity of a user or system. Authorization determines what that authenticated identity is permitted to access or do.'

    UNION ALL

    SELECT
        'Definition',
        'What is a firewall and why is it used?',
        'A firewall filters network traffic according to configured security rules. It helps restrict unwanted connections and permits traffic according to the security policy.'

    UNION ALL

    SELECT
        'Conceptual',
        'Why is multifactor authentication safer than using only a password?',
        'Multifactor authentication requires additional evidence of identity, such as a security key or authenticator approval. A stolen password alone may therefore be insufficient to access an account.'

    UNION ALL

    SELECT
        'Implementation',
        'A company allows employees to access internal resources remotely. Name two security measures that can help protect remote access.',
        'Suitable measures include multifactor authentication, a properly configured VPN, managed and updated devices, strong access controls and monitoring. The measures should match the company security requirements.'

    UNION ALL

    SELECT
        'Conceptual',
        'Why should operating systems and applications receive security updates regularly?',
        'Security updates may fix vulnerabilities that attackers could exploit. Applying updates reduces exposure to known weaknesses. Organisations should test and deploy updates promptly according to risk and operational requirements.'
) AS q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Cybersecurity Fundamentals B'
  AND i.category_id = (
      SELECT id
      FROM interview_categories
      WHERE name = 'Cybersecurity'
      LIMIT 1
  )
  AND i.difficulty = 'easy'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

COMMIT;


-- =========================================================
-- CYBERSECURITY MEDIUM INTERVIEWS
-- 2 interviews | 14 questions
-- Admin ID: 1
-- Plain ASCII text
-- =========================================================

START TRANSACTION;

-- =========================================================
-- CREATE INTERVIEW A
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Cybersecurity Security Analysis A',
    c.id,
    1,
    'Threat analysis, access control, vulnerability assessment and incident investigation.',
    'medium',
    TRUE,
    30
FROM interview_categories c
WHERE c.name = 'Cybersecurity'
AND NOT EXISTS (
    SELECT 1
    FROM interviews x
    WHERE x.interview_name = 'Cybersecurity Security Analysis A'
    AND x.category_id = c.id
);

-- =========================================================
-- CREATE INTERVIEW B
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Cybersecurity Security Analysis B',
    c.id,
    1,
    'Network defense, application security, monitoring and risk mitigation.',
    'medium',
    TRUE,
    30
FROM interview_categories c
WHERE c.name = 'Cybersecurity'
AND NOT EXISTS (
    SELECT 1
    FROM interviews x
    WHERE x.interview_name = 'Cybersecurity Security Analysis B'
    AND x.category_id = c.id
);

-- =========================================================
-- QUESTIONS FOR INTERVIEW A
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'medium',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Describe your understanding of cybersecurity and explain which security areas you want to develop your skills in.' AS question_text,
        'A good answer explains the purpose of cybersecurity, identifies areas such as network defense or application security, and describes a practical learning goal.' AS expected_answer

    UNION ALL

    SELECT
        'Experience',
        'Describe a situation in which you identified a security weakness during a project or learning exercise. How did you investigate it?',
        'A good answer explains the context, weakness identified, investigation process, corrective action and verification. A controlled learning exercise is acceptable.'

    UNION ALL

    SELECT
        'Definition',
        'What is threat modeling and how does it improve system security?',
        'Threat modeling is a structured process for identifying assets, possible attackers, attack paths and security weaknesses. It helps teams prioritize risks and select appropriate mitigations.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between vulnerability scanning and penetration testing?',
        'Vulnerability scanning identifies potential security weaknesses. Penetration testing uses authorized and controlled testing to validate weaknesses and assess their impact.'

    UNION ALL

    SELECT
        'Implementation',
        'A web application stores user passwords. What measures should protect those passwords?',
        'Passwords should be stored using a dedicated password hashing algorithm such as Argon2id, scrypt or bcrypt, with unique salts and appropriate work factors. Plaintext storage should be avoided. Rate limiting and multifactor authentication provide additional protection.'

    UNION ALL

    SELECT
        'Debugging',
        'A monitoring system reports repeated failed login attempts against several employee accounts. How would you investigate?',
        'Review authentication logs, source addresses, timestamps, targeted accounts and related events. Determine whether the activity resembles password spraying, brute force attempts or legitimate user errors. Apply proportionate controls and follow incident response procedures.'

    UNION ALL

    SELECT
        'Trade-off',
        'An address associated with suspicious activity may also serve legitimate customers. How would you decide whether to block it?',
        'Assess the evidence, confidence level, potential impact and business requirements. Consider temporary rate limits, targeted blocking, additional authentication or monitoring. Document the decision and review its effectiveness.'
) AS q ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Cybersecurity Security Analysis A'
AND i.difficulty = 'medium'
AND i.category_id = (
    SELECT id
    FROM interview_categories
    WHERE name = 'Cybersecurity'
    LIMIT 1
)
AND NOT EXISTS (
    SELECT 1
    FROM interview_questions oldq
    WHERE oldq.interview_id = i.id
    AND oldq.text = q.question_text
);

-- =========================================================
-- QUESTIONS FOR INTERVIEW B
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'medium',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Explain how you would approach securing a small business network containing employee computers, a file server and internet access.' AS question_text,
        'A good answer covers asset identification, secure configuration, network segmentation, access control, patching, endpoint protection, backups and monitoring.' AS expected_answer

    UNION ALL

    SELECT
        'Experience',
        'Describe a time you investigated a suspicious message, unexpected system behavior or a possible security incident. What steps did you take?',
        'A good answer describes the evidence, investigation steps, escalation decisions, containment where appropriate and lessons learned. A simulated incident is acceptable.'

    UNION ALL

    SELECT
        'Definition',
        'What is network segmentation and how does it reduce security risk?',
        'Network segmentation separates systems into distinct network zones using controls such as VLANs, firewalls and access rules. It limits unnecessary communication and can reduce attacker movement between systems.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between symmetric encryption and public key cryptography?',
        'Symmetric encryption uses the same secret key for encryption and decryption. Public key cryptography uses a related public and private key pair for operations such as key exchange and digital signatures. Both methods are often combined in secure protocols.'

    UNION ALL

    SELECT
        'Implementation',
        'A web application accepts a customer identifier in a request and retrieves account information. How should the application prevent access to another customer account?',
        'The server should authenticate the requester and enforce authorization for every account request. It should validate the requested resource and deny unauthorized access. Random identifiers alone do not provide authorization. Tests should cover attempts to access other accounts.'

    UNION ALL

    SELECT
        'Debugging',
        'A workstation repeatedly connects to an unfamiliar external server and triggers an endpoint security alert. How would you investigate and respond?',
        'Follow incident response procedures, collect endpoint and network evidence, identify the process and destination, and check related alerts. If compromise is suspected, isolate the device according to policy, preserve evidence, notify the security team and investigate the scope.'

    UNION ALL

    SELECT
        'Trade-off',
        'A company wants strict security controls, but employees report that these controls make normal work difficult. How would you balance security and usability?',
        'Identify the risks and the source of user friction. Apply controls proportionate to risk, consider safer alternatives and involve affected users. Test changes and monitor security outcomes and usability.'
) AS q ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Cybersecurity Security Analysis B'
AND i.difficulty = 'medium'
AND i.category_id = (
    SELECT id
    FROM interview_categories
    WHERE name = 'Cybersecurity'
    LIMIT 1
)
AND NOT EXISTS (
    SELECT 1
    FROM interview_questions oldq
    WHERE oldq.interview_id = i.id
    AND oldq.text = q.question_text
);

COMMIT;

-- =========================================================
-- CYBERSECURITY HARD INTERVIEW
-- 1 interview | 8 questions
-- Admin ID: 1
-- Plain ASCII text
-- =========================================================

START TRANSACTION;

-- =========================================================
-- 1. CREATE THE HARD INTERVIEW
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Cybersecurity Advanced Security A',
    c.id,
    1,
    'Advanced security architecture, incident response, threat analysis and risk management.',
    'hard',
    TRUE,
    40
FROM interview_categories c
WHERE c.name = 'Cybersecurity'
AND NOT EXISTS (
    SELECT 1
    FROM interviews existing
    WHERE existing.interview_name = 'Cybersecurity Advanced Security A'
    AND existing.category_id = c.id
);

-- =========================================================
-- 2. INSERT THE EIGHT QUESTIONS
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'hard',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    -- QUESTION 1: INTRODUCTION
    SELECT
        'Introduction' AS question_type,
        'Describe your approach to protecting a complex information system against security threats while maintaining availability and usability.' AS question_text,
        'A strong answer identifies critical assets, evaluates threats and risks, applies layered security controls and considers monitoring, recovery and business requirements. The candidate should explain how security decisions are prioritized and validated.' AS expected_answer

    UNION ALL

    -- QUESTION 2: EXPERIENCE
    SELECT
        'Experience',
        'Describe a challenging security investigation or security improvement you completed. How did you identify the root cause, manage risk and verify the outcome?',
        'A strong answer explains the context, evidence collected, root cause analysis, containment or corrective actions, validation and lessons learned. A controlled lab or academic exercise is acceptable when professional experience is unavailable.'

    UNION ALL

    -- QUESTION 3: TECHNICAL
    SELECT
        'Architecture',
        'How would you design a secure architecture for a public web application that handles sensitive customer information?',
        'A strong design includes network segmentation, secure identity and access controls, encryption in transit and at rest where appropriate, input validation, secure session handling, secrets management, logging, monitoring, backups and tested recovery procedures. Controls should be based on the threat model and data sensitivity.'

    UNION ALL

    -- QUESTION 4: TECHNICAL
    SELECT
        'Conceptual',
        'Explain how a zero trust security model differs from a traditional perimeter based security model.',
        'Zero trust does not assume that a user or device is trustworthy merely because it is inside a network. Access decisions use identity, device state, context and policy, with least privilege and ongoing verification. A traditional perimeter model relies more heavily on the boundary between internal and external networks.'

    UNION ALL

    -- QUESTION 5: TECHNICAL
    SELECT
        'Conceptual',
        'How would you evaluate whether an organization has an effective vulnerability management program?',
        'Evaluate asset inventory coverage, scanning frequency, severity and exploitability prioritization, remediation deadlines, exception handling, patch verification and reporting. Effective programs measure exposure reduction and remediation outcomes rather than simply counting detected vulnerabilities.'

    UNION ALL

    -- QUESTION 6: PROBLEM SOLVING
    SELECT
        'Debugging',
        'A security team detects suspicious administrator activity shortly after an employee account is compromised. What steps would you take to contain the incident and determine its scope?',
        'Follow the incident response plan, preserve relevant logs and evidence, contain compromised accounts and affected systems, revoke sessions or credentials as appropriate, and assess privilege escalation and lateral movement. Coordinate with incident responders, restore systems safely and document the findings.'

    UNION ALL

    -- QUESTION 7: PROBLEM SOLVING
    SELECT
        'Trade-off',
        'A critical production system contains a severe vulnerability, but applying the available patch may interrupt an essential business service. How would you manage this risk?',
        'Assess exploitability, exposure, business impact and available mitigations. Coordinate with system owners, test the patch, schedule an appropriate deployment window and apply temporary controls where needed. Escalate and document any delay, define a remediation deadline and verify the final result.'

    UNION ALL

    -- QUESTION 8: PROBLEM SOLVING
    SELECT
        'Debugging',
        'An organization discovers that sensitive data may have been transferred to an unauthorized external destination. How would you investigate the incident and reduce further exposure?',
        'Preserve relevant endpoint, network and access logs, establish the timeline and determine which data and systems were affected. Contain ongoing transfers, secure compromised accounts or systems, and coordinate legal, privacy and incident response teams as required. Validate the scope, follow applicable notification obligations and implement corrective measures.'
) AS q ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Cybersecurity Advanced Security A'
AND i.difficulty = 'hard'
AND i.category_id = (
    SELECT id
    FROM interview_categories
    WHERE name = 'Cybersecurity'
    LIMIT 1
)
AND NOT EXISTS (
    SELECT 1
    FROM interview_questions existing
    WHERE existing.interview_id = i.id
    AND existing.text = q.question_text
);

COMMIT;

-- =========================================================
-- DATA SCIENCE CONCEPTS
-- Initial concept library
-- Existing concepts are skipped
-- =========================================================

INSERT INTO concepts (name)
SELECT new_concepts.name
FROM (
    SELECT 'data-science-basics' AS name
    UNION ALL SELECT 'data-science-workflow'
    UNION ALL SELECT 'data-types'
    UNION ALL SELECT 'structured-data'
    UNION ALL SELECT 'unstructured-data'
    UNION ALL SELECT 'data-collection'
    UNION ALL SELECT 'data-quality'
    UNION ALL SELECT 'exploratory-data-analysis'
    UNION ALL SELECT 'descriptive-statistics'
    UNION ALL SELECT 'data-visualization'

    UNION ALL SELECT 'probability-theory'
    UNION ALL SELECT 'probability-distributions'
    UNION ALL SELECT 'mean-median-mode'
    UNION ALL SELECT 'variance-and-standard-deviation'
    UNION ALL SELECT 'sampling-methods'
    UNION ALL SELECT 'sampling-bias'
    UNION ALL SELECT 'correlation'
    UNION ALL SELECT 'statistical-significance'
    UNION ALL SELECT 'hypothesis-testing'
    UNION ALL SELECT 'confidence-intervals'

    UNION ALL SELECT 'data-cleaning'
    UNION ALL SELECT 'missing-data'
    UNION ALL SELECT 'outlier-detection'
    UNION ALL SELECT 'data-normalization'
    UNION ALL SELECT 'feature-scaling'
    UNION ALL SELECT 'categorical-encoding'
    UNION ALL SELECT 'feature-engineering'
    UNION ALL SELECT 'data-transformation'
    UNION ALL SELECT 'data-aggregation'
    UNION ALL SELECT 'data-leakage'

    UNION ALL SELECT 'python-data-science'
    UNION ALL SELECT 'numpy'
    UNION ALL SELECT 'pandas'
    UNION ALL SELECT 'dataframes'
    UNION ALL SELECT 'sql-for-data-analysis'
    UNION ALL SELECT 'data-filtering'
    UNION ALL SELECT 'data-joining'
    UNION ALL SELECT 'group-by-aggregation'
    UNION ALL SELECT 'matplotlib'
    UNION ALL SELECT 'seaborn'

    UNION ALL SELECT 'machine-learning-basics'
    UNION ALL SELECT 'supervised-learning'
    UNION ALL SELECT 'unsupervised-learning'
    UNION ALL SELECT 'classification'
    UNION ALL SELECT 'regression'
    UNION ALL SELECT 'clustering'
    UNION ALL SELECT 'training-and-test-data'
    UNION ALL SELECT 'cross-validation'
    UNION ALL SELECT 'overfitting-and-underfitting'
    UNION ALL SELECT 'model-evaluation'

    UNION ALL SELECT 'linear-regression'
    UNION ALL SELECT 'logistic-regression'
    UNION ALL SELECT 'decision-trees'
    UNION ALL SELECT 'random-forests'
    UNION ALL SELECT 'gradient-boosting'
    UNION ALL SELECT 'k-means-clustering'
    UNION ALL SELECT 'knn'
    UNION ALL SELECT 'support-vector-machines'
    UNION ALL SELECT 'dimensionality-reduction'
    UNION ALL SELECT 'principal-component-analysis'

    UNION ALL SELECT 'bias-variance-tradeoff'
    UNION ALL SELECT 'hyperparameter-tuning'
    UNION ALL SELECT 'precision-and-recall'
    UNION ALL SELECT 'confusion-matrix'
    UNION ALL SELECT 'roc-auc'
    UNION ALL SELECT 'class-imbalance'
    UNION ALL SELECT 'feature-selection'
    UNION ALL SELECT 'model-interpretability'
    UNION ALL SELECT 'ensemble-learning'
    UNION ALL SELECT 'model-drift'

    UNION ALL SELECT 'data-pipelines'
    UNION ALL SELECT 'etl-and-elt'
    UNION ALL SELECT 'batch-processing'
    UNION ALL SELECT 'stream-processing'
    UNION ALL SELECT 'data-warehousing'
    UNION ALL SELECT 'data-lakes'
    UNION ALL SELECT 'data-versioning'
    UNION ALL SELECT 'model-deployment'
    UNION ALL SELECT 'mlops'
    UNION ALL SELECT 'model-monitoring'
) AS new_concepts
WHERE NOT EXISTS (
    SELECT 1
    FROM concepts existing
    WHERE existing.name = new_concepts.name
);

-- =========================================================
-- DATA SCIENCE EASY INTERVIEWS
-- 2 interviews | 12 questions
-- Admin ID: 1
-- ASCII ONLY
-- =========================================================

START TRANSACTION;

-- =========================================================
-- CREATE INTERVIEW A
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Data Science Fundamentals A',
    c.id,
    1,
    'Introduction to data science, data quality and statistics.',
    'easy',
    TRUE,
    20
FROM interview_categories c
WHERE c.name = 'Data Science'
AND NOT EXISTS (
    SELECT 1
    FROM interviews x
    WHERE x.interview_name = 'Data Science Fundamentals A'
    AND x.category_id = c.id
);

-- =========================================================
-- CREATE INTERVIEW B
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Data Science Fundamentals B',
    c.id,
    1,
    'Introduction to Python, pandas, data cleaning and visualization.',
    'easy',
    TRUE,
    20
FROM interview_categories c
WHERE c.name = 'Data Science'
AND NOT EXISTS (
    SELECT 1
    FROM interviews x
    WHERE x.interview_name = 'Data Science Fundamentals B'
    AND x.category_id = c.id
);

-- =========================================================
-- QUESTIONS FOR INTERVIEW A
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'easy',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Introduce yourself and explain why you are interested in data science.' AS question_text,
        'A good answer describes an interest in working with data, relevant learning or experience, and a desire to find patterns or support decisions using evidence.' AS expected_answer

    UNION ALL

    SELECT
        'Definition',
        'What is data science and what problems can it help solve?',
        'Data science combines statistics, programming, data analysis and domain knowledge to extract useful insights from data. It supports forecasting, decision making, pattern detection and problem solving.'

    UNION ALL

    SELECT
        'Definition',
        'What is the difference between structured and unstructured data?',
        'Structured data follows a defined format, such as rows and columns in a database. Unstructured data does not follow the same fixed tabular structure and includes images, audio and free text.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between the mean and the median, and when is the median more useful?',
        'The mean is the sum of values divided by the number of values. The median is the middle value after sorting the data. The median is often more useful when extreme values affect the mean.'

    UNION ALL

    SELECT
        'Implementation',
        'A dataset contains ages of 18, 20, 22, 24 and 26. How would you calculate the mean age?',
        'Add the values to get 110 and divide by 5. The mean age is 22.'

    UNION ALL

    SELECT
        'Conceptual',
        'Why is data quality important before performing an analysis?',
        'Poor data quality can produce misleading results. Analysts should check missing values, duplicates, inconsistent formats, invalid values and measurement errors before drawing conclusions.'
) AS q ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Data Science Fundamentals A'
AND i.difficulty = 'easy'
AND i.category_id = (
    SELECT id
    FROM interview_categories
    WHERE name = 'Data Science'
    LIMIT 1
)
AND NOT EXISTS (
    SELECT 1
    FROM interview_questions oldq
    WHERE oldq.interview_id = i.id
    AND oldq.text = q.question_text
);

-- =========================================================
-- QUESTIONS FOR INTERVIEW B
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'easy',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Describe your experience with Python or other tools used to analyze data.' AS question_text,
        'A good answer describes programming experience, tools used, relevant data analysis projects and areas the candidate wants to develop. Learning projects are acceptable.' AS expected_answer

    UNION ALL

    SELECT
        'Definition',
        'What is pandas in Python and why is it useful for data science?',
        'Pandas is a Python library for working with structured data. It provides Series and DataFrame objects and supports data cleaning, filtering, grouping, joining and analysis.'

    UNION ALL

    SELECT
        'Definition',
        'What is exploratory data analysis and why is it performed?',
        'Exploratory data analysis examines and summarizes a dataset to understand its structure, distributions, relationships, unusual values and potential quality issues before drawing conclusions or building models.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between a DataFrame and a Series in pandas?',
        'A DataFrame is a two dimensional labeled data structure with rows and columns. A Series is a one dimensional labeled array. A DataFrame column is commonly represented as a Series.'

    UNION ALL

    SELECT
        'Implementation',
        'You have a pandas DataFrame named df with a column called age. How would you select rows where age is greater than 25?',
        'Use df[df["age"] > 25]. This returns rows whose age value is greater than 25. Missing age values do not satisfy this comparison.'

    UNION ALL

    SELECT
        'Conceptual',
        'Why use a chart when analyzing a dataset instead of relying only on a table of numbers?',
        'Charts make patterns, trends, distributions, relationships and unusual values easier to identify. The chart type should match the data and question, with clear labels and scales.'
) AS q ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Data Science Fundamentals B'
AND i.difficulty = 'easy'
AND i.category_id = (
    SELECT id
    FROM interview_categories
    WHERE name = 'Data Science'
    LIMIT 1
)
AND NOT EXISTS (
    SELECT 1
    FROM interview_questions oldq
    WHERE oldq.interview_id = i.id
    AND oldq.text = q.question_text
);

COMMIT;

-- =========================================================
-- DATA SCIENCE MEDIUM INTERVIEWS
-- 2 interviews | 14 questions
-- Admin ID: 1
-- ASCII ONLY
-- =========================================================

START TRANSACTION;

-- =========================================================
-- CREATE INTERVIEW A
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Data Science Applied Analysis A',
    c.id,
    1,
    'Data cleaning, statistical analysis, feature engineering and model evaluation.',
    'medium',
    TRUE,
    30
FROM interview_categories c
WHERE c.name = 'Data Science'
AND NOT EXISTS (
    SELECT 1
    FROM interviews x
    WHERE x.interview_name = 'Data Science Applied Analysis A'
    AND x.category_id = c.id
);

-- =========================================================
-- CREATE INTERVIEW B
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Data Science Applied Analysis B',
    c.id,
    1,
    'Exploratory analysis, statistical testing, machine learning and data quality.',
    'medium',
    TRUE,
    30
FROM interview_categories c
WHERE c.name = 'Data Science'
AND NOT EXISTS (
    SELECT 1
    FROM interviews x
    WHERE x.interview_name = 'Data Science Applied Analysis B'
    AND x.category_id = c.id
);

-- =========================================================
-- QUESTIONS FOR INTERVIEW A
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'medium',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Explain how you would approach a new dataset when the business question is not fully defined.' AS question_text,
        'A good answer describes clarifying the business objective, identifying available data, checking data quality, exploring distributions and relationships, and communicating initial findings before selecting an analysis method.' AS expected_answer

    UNION ALL

    SELECT
        'Experience',
        'Describe a data analysis task where the initial results were misleading or unexpected. How did you investigate and improve the analysis?',
        'A good answer explains the original problem, checks performed, possible causes such as missing values or sampling bias, corrective actions and how the revised results were validated.'

    UNION ALL

    SELECT
        'Definition',
        'What is data leakage in machine learning and why can it cause problems?',
        'Data leakage occurs when information unavailable at prediction time influences model training or evaluation. It can produce overly optimistic results that do not reflect real world performance. Prevention includes correct data splitting and fitting preprocessing steps only on training data.'

    UNION ALL

    SELECT
        'Conceptual',
        'Explain the difference between classification and regression, and give one example of each.',
        'Classification predicts categories, such as whether a transaction is fraudulent. Regression predicts numerical values, such as the expected price of a house.'

    UNION ALL

    SELECT
        'Implementation',
        'A pandas DataFrame contains missing values in several columns. How would you investigate and decide how to handle them?',
        'Inspect missing value counts and proportions, determine whether missingness follows a pattern, and assess the meaning of each column. Depending on the data and objective, options include removing affected rows, removing unsuitable columns or imputing values. The approach should be validated to avoid bias and leakage.'

    UNION ALL

    SELECT
        'Debugging',
        'A machine learning model performs very well on training data but poorly on unseen test data. What would you investigate first?',
        'Investigate overfitting, training and test data differences, data leakage, model complexity and the suitability of the evaluation method. Use cross validation where appropriate and compare training and validation performance before choosing corrective actions.'

    UNION ALL

    SELECT
        'Trade-off',
        'A model with many features performs slightly better during evaluation but is slower and harder to explain than a simpler model. How would you choose between them?',
        'Compare performance using appropriate metrics, inference cost, interpretability, maintenance and business risk. If the performance improvement is not meaningful, the simpler model may be preferable. The decision should reflect the application requirements and be validated on representative data.'
) AS q ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Data Science Applied Analysis A'
AND i.difficulty = 'medium'
AND i.category_id = (
    SELECT id
    FROM interview_categories
    WHERE name = 'Data Science'
    LIMIT 1
)
AND NOT EXISTS (
    SELECT 1
    FROM interview_questions oldq
    WHERE oldq.interview_id = i.id
    AND oldq.text = q.question_text
);

-- =========================================================
-- QUESTIONS FOR INTERVIEW B
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'medium',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    SELECT
        'Introduction' AS question_type,
        'How would you use data to help a business understand why customer retention has declined?' AS question_text,
        'A good answer defines the retention measure, identifies relevant customer and time period data, checks data quality, compares customer groups and investigates possible explanations before recommending further analysis.' AS expected_answer

    UNION ALL

    SELECT
        'Experience',
        'Describe a project or practical exercise where you used statistics, Python or machine learning to answer a question. How did you evaluate your results?',
        'A good answer explains the objective, data preparation, method selected, evaluation approach, results and limitations. A personal or academic project is acceptable.'

    UNION ALL

    SELECT
        'Definition',
        'What is hypothesis testing and what role does a p value play?',
        'Hypothesis testing evaluates evidence against a stated null hypothesis. A p value measures how likely results at least as extreme as those observed would be if the null hypothesis and its assumptions were true. It does not directly measure the probability that the null hypothesis is true.'

    UNION ALL

    SELECT
        'Conceptual',
        'Why should a dataset be split into training, validation and test sets when developing a machine learning model?',
        'The training set is used to fit the model. The validation set helps compare models and tune settings. The test set provides a final evaluation on data not used for those decisions. This separation reduces optimistic estimates of model performance.'

    UNION ALL

    SELECT
        'Implementation',
        'You have sales data with columns for region and revenue. How could you calculate total revenue for each region using pandas?',
        'Use df.groupby("region")["revenue"].sum(). This groups rows by region and calculates the total revenue for each group. Missing or invalid revenue values should be checked before interpreting the result.'

    UNION ALL

    SELECT
        'Debugging',
        'A dataset has a strong correlation between two variables, but a business team concludes that one variable causes the other. How would you assess that conclusion?',
        'Explain that correlation alone does not establish causation. Investigate confounding variables, reverse causality, selection effects and the data collection method. Stronger causal claims may require a randomized experiment or an appropriate causal inference design.'

    UNION ALL

    SELECT
        'Trade-off',
        'Your dataset contains very few examples of a rare but important event. A model achieves high accuracy but misses most of those events. How would you improve the evaluation and model?',
        'Accuracy can be misleading with imbalanced classes. Examine the confusion matrix, recall, precision and precision recall curves. Consider class weighting, resampling or suitable algorithms using training data only. Select a decision threshold based on the relative costs of false positives and false negatives, then evaluate on representative held out data.'
) AS q ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Data Science Applied Analysis B'
AND i.difficulty = 'medium'
AND i.category_id = (
    SELECT id
    FROM interview_categories
    WHERE name = 'Data Science'
    LIMIT 1
)
AND NOT EXISTS (
    SELECT 1
    FROM interview_questions oldq
    WHERE oldq.interview_id = i.id
    AND oldq.text = q.question_text
);

COMMIT;

-- =========================================================
-- DATA SCIENCE HARD INTERVIEW
-- 1 interview | 8 questions
-- Admin ID: 1
-- ASCII ONLY
-- =========================================================

START TRANSACTION;

-- =========================================================
-- CREATE THE HARD INTERVIEW
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Data Science Advanced Analysis A',
    c.id,
    1,
    'Advanced machine learning, statistical reasoning, model evaluation and production data science.',
    'hard',
    TRUE,
    40
FROM interview_categories c
WHERE c.name = 'Data Science'
AND NOT EXISTS (
    SELECT 1
    FROM interviews existing
    WHERE existing.interview_name = 'Data Science Advanced Analysis A'
    AND existing.category_id = c.id
);

-- =========================================================
-- INSERT THE EIGHT QUESTIONS
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'hard',
    q.question_text,
    q.expected_answer,
    TRUE
FROM interviews i
JOIN (
    -- QUESTION 1: INTRODUCTION
    SELECT
        'Introduction' AS question_type,
        'Explain how you would design a data science solution for a business problem where the available data is incomplete and the cost of incorrect predictions is high.' AS question_text,
        'A strong answer starts by clarifying the business objective and the cost of different errors. It assesses data quality, representativeness and limitations, selects appropriate analysis and evaluation methods, establishes a baseline, and explains how uncertainty and risk will influence deployment decisions.' AS expected_answer

    UNION ALL

    -- QUESTION 2: EXPERIENCE
    SELECT
        'Experience',
        'Describe a challenging data science project in which your initial approach did not produce reliable results. How did you identify the problem and improve the solution?',
        'A strong answer describes the original objective, the evidence that results were unreliable, the investigation and root cause, the changes made and the final validation. Relevant examples include data leakage, sampling bias, unsuitable metrics or incorrect assumptions. A practical or academic project is acceptable.'

    UNION ALL

    -- QUESTION 3: TECHNICAL
    SELECT
        'Conceptual',
        'Explain the bias variance tradeoff and how it influences the choice of a machine learning model.',
        'Bias is error associated with assumptions that are too restrictive, while variance describes sensitivity to changes in training data. Models with excessive bias may underfit, while models with excessive variance may overfit. Cross validation, suitable model complexity, regularization and representative data help balance the two.'

    UNION ALL

    -- QUESTION 4: TECHNICAL
    SELECT
        'Implementation',
        'You are building a model to predict customer churn using historical records. Explain how you would prevent data leakage when preparing features and evaluating the model.',
        'Define the prediction time and ensure every feature would have been available at that time. Split data before fitting preprocessing steps, fit transformations only on training data, and use pipelines to apply the same transformations to validation and test data. Use time based splitting when appropriate and exclude future information such as cancellation outcomes recorded after the prediction date.'

    UNION ALL

    -- QUESTION 5: TECHNICAL
    SELECT
        'Architecture',
        'How would you design a machine learning pipeline that remains reliable when incoming data distributions and business requirements change over time?',
        'A strong design includes versioned data and models, repeatable preprocessing, automated validation, reproducible training, deployment controls, performance and data quality monitoring, drift detection and rollback procedures. It should define retraining criteria and evaluate candidate models before replacing a production model.'

    UNION ALL

    -- QUESTION 6: PROBLEM SOLVING
    SELECT
        'Debugging',
        'A model has excellent offline test results but its predictions become inaccurate after deployment. How would you investigate the cause?',
        'Check whether production data differs from training data, whether feature definitions or preprocessing changed, and whether training data leakage or an incorrect evaluation split inflated offline results. Examine prediction and outcome logs, data quality, concept drift and delayed labels. Reproduce the issue, establish a baseline and validate corrective changes before redeployment.'

    UNION ALL

    -- QUESTION 7: PROBLEM SOLVING
    SELECT
        'Trade-off',
        'A fraud detection model must identify as many fraudulent transactions as possible while keeping false alarms manageable. How would you choose the model evaluation metrics and decision threshold?',
        'Use the confusion matrix, precision, recall and precision recall curves to understand the tradeoff. Estimate the operational cost of missed fraud and false alarms, select a threshold aligned with those costs and validate it on representative data. Monitor performance after deployment because fraud patterns and class proportions may change.'

    UNION ALL

    -- QUESTION 8: PROBLEM SOLVING
    SELECT
        'Debugging',
        'An analysis suggests that a new business policy increased customer retention, but retention also improved in the same period across the wider market. How would you determine whether the policy caused the improvement?',
        'A strong answer distinguishes correlation from causation. If possible, use a randomized controlled experiment. Otherwise consider a suitable quasi experimental method such as difference in differences with a credible comparison group and defensible assumptions. Check pre intervention trends, confounding factors, selection bias and uncertainty before attributing the change to the policy.'
) AS q ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Data Science Advanced Analysis A'
AND i.difficulty = 'hard'
AND i.category_id = (
    SELECT id
    FROM interview_categories
    WHERE name = 'Data Science'
    LIMIT 1
)
AND NOT EXISTS (
    SELECT 1
    FROM interview_questions existing
    WHERE existing.interview_id = i.id
    AND existing.text = q.question_text
);

COMMIT;