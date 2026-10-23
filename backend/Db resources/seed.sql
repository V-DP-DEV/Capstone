INSERT INTO users (email,password,role) VALUES ('admin@example.com','$2y$10$c0RNyM2qGHFbrugopNNNYOGPrkGx32VgLZGn7aMRU7Fg.AE1gZe0i','ADMIN');
INSERT INTO users (email,password,role) VALUES ('admin2@example.com','$2y$10$c0RNyM2qGHFbrugopNNNYOGPrkGx32VgLZGn7aMRU7Fg.AE1gZe0i','ADMIN');
INSERT INTO users (email,password,role) VALUES ('user@example.com','$2y$10$c0RNyM2qGHFbrugopNNNYOGPrkGx32VgLZGn7aMRU7Fg.AE1gZe0i','USER');
INSERT INTO users (email,password,role) VALUES ('user2@example.com','$2y$10$c0RNyM2qGHFbrugopNNNYOGPrkGx32VgLZGn7aMRU7Fg.AE1gZe0i','USER');
INSERT INTO users (email,password,role) VALUES ('user3@example.com','$2y$10$c0RNyM2qGHFbrugopNNNYOGPrkGx32VgLZGn7aMRU7Fg.AE1gZe0i','USER');

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

-- =========================================================
-- CONCEPTS
-- =========================================================

INSERT INTO concepts (name) VALUES
-- C# and .NET
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

-- Python and LINQ
('python-basics'),
('linq'),
('deferred-execution'),
('entity-framework'),
('linq-filtering'),
('query-optimization'),

-- SQL and databases
('sql-indexing'),
('sql-query-performance'),
('sql-execution-plans'),
('sql-joins'),
('sql-views'),
('database-abstraction'),
('read-models'),
('controller-service-pattern'),
('data-access-layer'),
('sql-transactions'),
('atomicity'),
('rollback'),
('data-consistency'),
('database-access'),
('sql-data-access'),

-- ASP.NET Core and APIs
('http-request-pipeline'),
('aspnet-core-middleware'),
('routing'),
('rest-api'),
('http-status-codes'),
('layered-architecture'),
('api-contracts'),
('input-validation'),
('request-tracing'),
('api-validation'),
('request-response-cycle'),
('idempotency'),

-- Frontend and full-stack development
('frontend-state'),
('browser-caching'),
('api-caching'),
('performance-profiling'),
('full-stack-development'),
('frontend-integration'),
('javascript'),
('javascript-fetch'),
('error-handling'),
('network-debugging'),
('duplicate-requests'),
('frontend-architecture'),
('api-client'),
('state-management'),
('full-stack-architecture'),

-- Java and Spring
('java-basics'),
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

-- Architecture and design
('dto'),
('entity-exposure'),
('domain-model-separation'),
('data-projection'),
('performance-trade-offs'),
('maintainability'),
('concurrency'),
('message-processing'),
('distributed-caching'),

-- Data science fundamentals
('data-science-basics'),
('data-science-workflow'),
('data-types'),
('structured-data'),
('unstructured-data'),
('data-collection'),
('data-quality'),
('exploratory-data-analysis'),
('descriptive-statistics'),
('data-visualization'),

-- Statistics and probability
('probability-theory'),
('probability-distributions'),
('mean-median-mode'),
('variance-and-standard-deviation'),
('sampling-methods'),
('sampling-bias'),
('correlation'),
('statistical-significance'),
('hypothesis-testing'),
('confidence-intervals'),

-- Data preparation
('data-cleaning'),
('missing-data'),
('outlier-detection'),
('data-normalization'),
('feature-scaling'),
('categorical-encoding'),
('feature-engineering'),
('data-transformation'),
('data-aggregation'),
('data-leakage'),

-- Data science tools and analysis
('python-data-science'),
('numpy'),
('pandas'),
('dataframes'),
('sql-for-data-analysis'),
('data-filtering'),
('data-joining'),
('group-by-aggregation'),
('matplotlib'),
('seaborn'),

-- Machine learning fundamentals
('machine-learning-basics'),
('supervised-learning'),
('unsupervised-learning'),
('classification'),
('regression'),
('clustering'),
('training-and-test-data'),
('cross-validation'),
('overfitting-and-underfitting'),
('model-evaluation'),

-- Machine learning algorithms
('linear-regression'),
('logistic-regression'),
('decision-trees'),
('random-forests'),
('gradient-boosting'),
('k-means-clustering'),
('knn'),
('support-vector-machines'),
('dimensionality-reduction'),
('principal-component-analysis'),

-- Model optimization and evaluation
('bias-variance-tradeoff'),
('hyperparameter-tuning'),
('precision-and-recall'),
('confusion-matrix'),
('roc-auc'),
('class-imbalance'),
('feature-selection'),
('model-interpretability'),
('ensemble-learning'),
('model-drift'),

-- Data engineering and MLOps
('data-pipelines'),
('etl-and-elt'),
('batch-processing'),
('stream-processing'),
('data-warehousing'),
('data-lakes'),
('data-versioning'),
('model-deployment'),
('mlops'),
('model-monitoring'),
('cybersecurity-basics'),
('cia-triad'),
('security-principles'),
('threat-modeling'),
('attack-surface'),
('threat-actors'),
('security-vulnerabilities'),
('security-risk-assessment'),
('defense-in-depth'),
('least-privilege'),

-- Network Security and Cryptography
('network-security'),
('tcp-ip'),
('dns-security'),
('firewall-rules'),
('network-segmentation'),
('vpn'),
('tls-https'),
('public-key-cryptography'),
('symmetric-encryption'),
('hashing'),
('digital-signatures'),
('certificate-management'),

-- Web Application Security
('owasp-top-ten'),
('sql-injection'),
('cross-site-scripting'),
('cross-site-request-forgery'),
('server-side-request-forgery'),
('secure-coding'),
('output-encoding'),
('security-headers'),
('secure-session-management'),
('secrets-management'),

-- Identity and Access Management
('multi-factor-authentication'),
('identity-management'),
('role-based-access-control'),
('privileged-access-management'),
('password-security'),
('oauth2'),
('openid-connect'),
('session-security'),

-- Security Monitoring and Incident Response
('security-logging'),
('security-monitoring'),
('siem'),
('intrusion-detection'),
('endpoint-detection-response'),
('vulnerability-scanning'),
('patch-management'),
('incident-response'),
('digital-forensics'),
('malware-analysis'),

-- Security Governance and Operations
('secure-software-development-lifecycle'),
('penetration-testing'),
('risk-mitigation'),
('security-auditing'),
('backup-and-recovery'),
('business-continuity'),
('data-classification'),
('security-compliance');




-- =========================================================
-- JAVA EASY INTERVIEW
-- 1 interview | 6 questions
-- Admin ID: 1
-- =========================================================

-- =========================================================
-- 1. CREATE JAVA EASY INTERVIEW
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Java Fundamentals Beginner',
    c.id,
    1,
    'An introductory Java interview covering core concepts, object oriented programming and basic implementation.',
    'easy',
    TRUE,
    20
FROM interview_categories c
WHERE c.name = 'Java'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews i
      WHERE i.interview_name = 'Java Fundamentals Beginner'
        AND i.category_id = c.id
  );

-- =========================================================
-- 2. INSERT INTERVIEW QUESTIONS
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
JOIN interview_categories c
    ON c.id = i.category_id
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Tell me about yourself and your experience learning or using Java.' AS question_text,
        'A good answer introduces the candidate, describes their Java learning or development experience, mentions a relevant project and explains their current goals.' AS expected_answer

    UNION ALL

    SELECT
        'Definition',
        'What is Java and what are its main characteristics?',
        'Java is a high level, class based programming language. Its main characteristics include object oriented programming, static typing, automatic memory management and execution through the Java Virtual Machine.'

    UNION ALL

    SELECT
        'Definition',
        'What is the difference between a class and an object in Java?',
        'A class defines the structure and behaviour of a type. An object is an instance of a class created at runtime. Objects can maintain their own instance state.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is encapsulation and why is it useful in Java?',
        'Encapsulation combines data and behaviour within a class and controls access to internal state. Private fields and public methods can protect object invariants and improve maintainability.'

    UNION ALL

    SELECT
        'Implementation',
        'How would you write a Java method that checks whether an integer is even?',
        'One valid implementation is boolean isEven(int number) { return number % 2 == 0; }. The modulo operator returns the remainder after division by two.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between an int and a String in Java?',
        'int is a primitive type used to represent whole numbers. String is a reference type used to represent text and is implemented as an immutable class.'
) q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Java Fundamentals Beginner'
  AND c.name = 'Java'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

-- =========================================================
-- 3. MAP QUESTIONS TO EXISTING CONCEPTS
-- =========================================================

INSERT INTO interview_question_concepts
(question_id, concept_id)
SELECT
    iq.id,
    co.id
FROM interview_questions iq
JOIN interviews i
    ON i.id = iq.interview_id
JOIN (
    SELECT
        'Tell me about yourself and your experience learning or using Java.' AS question_text,
        'java-basics' AS concept_name

    UNION ALL

    SELECT
        'What is Java and what are its main characteristics?',
        'java-basics'

    UNION ALL

    SELECT
        'What is the difference between a class and an object in Java?',
        'java-basics'

    UNION ALL

    SELECT
        'What is encapsulation and why is it useful in Java?',
        'java-basics'

    UNION ALL

    SELECT
        'How would you write a Java method that checks whether an integer is even?',
        'java-basics'

    UNION ALL

    SELECT
        'What is the difference between an int and a String in Java?',
        'java-basics'
) mapping
    ON mapping.question_text = iq.text
JOIN concepts co
    ON co.name = mapping.concept_name
WHERE i.interview_name = 'Java Fundamentals Beginner'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_question_concepts existing
      WHERE existing.question_id = iq.id
        AND existing.concept_id = co.id
  );























-- =========================================================
-- JAVA MEDIUM INTERVIEW
-- 1 interview | 7 questions
-- 2 to 3 concepts per question
-- Admin ID: 1
-- =========================================================

-- =========================================================
-- 1. CREATE INTERVIEW
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Java Intermediate Problem Solving',
    c.id,
    1,
    'An intermediate Java interview covering object oriented design, collections, persistence, API development and practical problem solving.',
    'medium',
    TRUE,
    30
FROM interview_categories c
WHERE c.name = 'Java'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'Java Intermediate Problem Solving'
        AND existing.category_id = c.id
  );

-- =========================================================
-- 2. INSERT QUESTIONS
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
JOIN interview_categories c
    ON c.id = i.category_id
CROSS JOIN (
    SELECT
        'Introduction' AS question_type,
        'Walk me through a Java application you have built or studied and explain how its main components work together.' AS question_text,
        'A good answer describes the application purpose, its main components, how data flows between them and the candidates own contribution or learning.' AS expected_answer

    UNION ALL

    SELECT
        'Experience',
        'Describe a situation where you had to improve an existing Java implementation without changing its expected behaviour.',
        'A good answer explains how the candidate understood the existing behaviour, identified the improvement opportunity, considered regression risks, made targeted changes and validated the result with suitable tests.'

    UNION ALL

    SELECT
        'Definition',
        'What is the repository pattern in Java and why might an application use it?',
        'The repository pattern separates data access logic from business logic. It provides a consistent interface for retrieving and persisting data, improves separation of concerns and can make application code easier to test.'

    UNION ALL

    SELECT
        'Conceptual',
        'How do Java HashMap and ArrayList differ in how they store and retrieve data, and when would you choose each?',
        'ArrayList stores an ordered sequence of elements and supports indexed access. HashMap stores key value associations and provides efficient average lookup by key. The choice depends on whether the application needs ordered elements or lookup by unique keys.'

    UNION ALL

    SELECT
        'Implementation',
        'How would you use Java Streams to group a list of orders by customer identifier and count the orders for each customer?',
        'A suitable solution is orders.stream().collect(Collectors.groupingBy(Order::getCustomerId, Collectors.counting())). This groups orders by customer identifier and counts the orders in each group.'

    UNION ALL

    SELECT
        'Debugging',
        'A Java REST endpoint occasionally returns duplicate records after a user refreshes the page. How would you investigate the cause and prevent the duplicates?',
        'A strong answer checks browser requests, API logs, repeated submissions, service logic and database constraints. The candidate should identify whether duplication occurs in the client, request handling or persistence layer and apply an appropriate fix such as preventing duplicate requests, using idempotency or enforcing suitable uniqueness constraints.'

    UNION ALL

    SELECT
        'Trade-off',
        'A Java application needs to process a large collection of records. Would you process everything in memory or retrieve and process records in batches? Explain how you would decide.',
        'A strong answer considers collection size, available memory, processing time, database load, network overhead and failure recovery. Batching can reduce memory pressure and support large workloads, while in memory processing may be simpler and faster for suitably sized datasets.'
) AS q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Java Intermediate Problem Solving'
  AND c.name = 'Java'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

-- =========================================================
-- 3. MAP QUESTIONS TO EXISTING CONCEPTS
-- Each question receives 2 or 3 concepts
-- =========================================================

INSERT INTO interview_question_concepts
(question_id, concept_id)
SELECT
    iq.id,
    co.id
FROM interview_questions iq
JOIN interviews i
    ON i.id = iq.interview_id
JOIN (
    SELECT
        'Walk me through a Java application you have built or studied and explain how its main components work together.' AS question_text,
        'java-basics' AS concept_name

    UNION ALL

    SELECT
        'Walk me through a Java application you have built or studied and explain how its main components work together.',
        'layered-architecture'

    UNION ALL

    SELECT
        'Describe a situation where you had to improve an existing Java implementation without changing its expected behaviour.',
        'maintainability'

    UNION ALL

    SELECT
        'Describe a situation where you had to improve an existing Java implementation without changing its expected behaviour.',
        'testability'

    UNION ALL

    SELECT
        'What is the repository pattern in Java and why might an application use it?',
        'repository-pattern'

    UNION ALL

    SELECT
        'What is the repository pattern in Java and why might an application use it?',
        'data-access-layer'

    UNION ALL

    SELECT
        'How do Java HashMap and ArrayList differ in how they store and retrieve data, and when would you choose each?',
        'java-basics'

    UNION ALL

    SELECT
        'How do Java HashMap and ArrayList differ in how they store and retrieve data, and when would you choose each?',
        'maintainability'

    UNION ALL

    SELECT
        'How would you use Java Streams to group a list of orders by customer identifier and count the orders for each customer?',
        'java-streams'

    UNION ALL

    SELECT
        'How would you use Java Streams to group a list of orders by customer identifier and count the orders for each customer?',
        'stream-collect'

    UNION ALL

    SELECT
        'How would you use Java Streams to group a list of orders by customer identifier and count the orders for each customer?',
        'stream-terminal-operations'

    UNION ALL

    SELECT
        'A Java REST endpoint occasionally returns duplicate records after a user refreshes the page. How would you investigate the cause and prevent the duplicates?',
        'duplicate-requests'

    UNION ALL

    SELECT
        'A Java REST endpoint occasionally returns duplicate records after a user refreshes the page. How would you investigate the cause and prevent the duplicates?',
        'idempotency'

    UNION ALL

    SELECT
        'A Java REST endpoint occasionally returns duplicate records after a user refreshes the page. How would you investigate the cause and prevent the duplicates?',
        'sql-transactions'

    UNION ALL

    SELECT
        'A Java application needs to process a large collection of records. Would you process everything in memory or retrieve and process records in batches? Explain how you would decide.',
        'batch-processing'

    UNION ALL

    SELECT
        'A Java application needs to process a large collection of records. Would you process everything in memory or retrieve and process records in batches? Explain how you would decide.',
        'performance-trade-offs'

    UNION ALL

    SELECT
        'A Java application needs to process a large collection of records. Would you process everything in memory or retrieve and process records in batches? Explain how you would decide.',
        'query-optimization'
) AS mapping
    ON mapping.question_text = iq.text
JOIN concepts co
    ON co.name = mapping.concept_name
WHERE i.interview_name = 'Java Intermediate Problem Solving'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_question_concepts existing
      WHERE existing.question_id = iq.id
        AND existing.concept_id = co.id
  );

































 -- =========================================================
-- JAVA HARD INTERVIEW
-- 1 interview | 8 questions
-- 3 to 4 concepts per question
-- Admin ID: 1
-- =========================================================

-- =========================================================
-- 1. CREATE INTERVIEW
-- =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Java Advanced Engineering',
    c.id,
    1,
    'An advanced Java interview covering architecture, concurrency, persistence performance, distributed systems and complex problem solving.',
    'hard',
    TRUE,
    45
FROM interview_categories c
WHERE c.name = 'Java'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'Java Advanced Engineering'
        AND existing.category_id = c.id
  );

-- =========================================================
-- 2. INSERT QUESTIONS
-- =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'hard',
    q.question_text,
    q.answer_text,
    TRUE
FROM interviews i
JOIN interview_categories c
    ON c.id = i.category_id
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Explain how you would design a production grade Java service that must remain maintainable, reliable and responsive under heavy traffic.' AS question_text,
        'A strong answer discusses clear architectural boundaries, dependency management, observability, resilience, testing, performance measurement and operational requirements. The candidate should explain how these choices work together and identify relevant trade offs.' AS answer_text

    UNION ALL

    SELECT
        'Experience',
        'Describe a complex Java performance or reliability issue you investigated. Explain how you identified the root cause, evaluated possible solutions and measured the outcome.',
        'A strong answer gives a structured account of the symptoms, evidence gathered, root cause, alternatives considered, implementation and measurable outcome. It should distinguish observed facts from assumptions and explain any remaining risks.'

    UNION ALL

    SELECT
        'Architecture',
        'How would you structure a Java application that needs to support multiple database implementations while keeping business rules independent of persistence details?',
        'A strong answer separates domain and business logic from infrastructure concerns, defines appropriate interfaces, uses dependency inversion and injects persistence implementations. It also discusses transaction boundaries, testing and the cost of additional abstraction.'

    UNION ALL

    SELECT
        'Conceptual',
        'Explain how Java memory visibility and thread safety differ, and describe how volatile, synchronized and atomic classes address concurrency problems.',
        'Volatile provides visibility and ordering guarantees for access to a variable but does not make compound operations atomic. Synchronized provides mutual exclusion and happens before guarantees. Atomic classes support thread safe atomic operations through compare and set mechanisms. The correct choice depends on the required invariant and contention.'

    UNION ALL

    SELECT
        'Implementation',
        'How would you implement a reliable background processing service in Java that handles concurrent tasks, limits resource usage and supports graceful shutdown?',
        'A strong solution uses a suitably configured ExecutorService or ThreadPoolExecutor, bounded queues, controlled rejection handling, explicit task error handling and graceful shutdown with a timeout. It should also address interruption, resource cleanup, back pressure and monitoring.'

    UNION ALL

    SELECT
        'Debugging',
        'A Java application experiences intermittent deadlocks after a new feature introduces several shared locks. How would you diagnose the issue and prevent it from recurring?',
        'A strong answer captures thread dumps, identifies threads waiting on locks and reconstructs the lock dependency cycle. Prevention may involve consistent lock ordering, smaller synchronized sections, reducing shared mutable state, using suitable concurrent utilities and adding stress tests.'

    UNION ALL

    SELECT
        'Trade-off',
        'A Java service must process millions of database records within a limited time window. How would you choose between parallel processing, batched queries and asynchronous processing?',
        'A strong answer measures the workload and identifies whether the bottleneck is CPU, database access, network latency or memory. It evaluates batch size, connection pool limits, parallelism, ordering requirements, failure recovery and transaction boundaries. The chosen design should be benchmarked under realistic load.'

    UNION ALL

    SELECT
        'Architecture',
        'A distributed Java application sometimes processes the same incoming event more than once when consumers restart. How would you design the processing flow to protect data consistency without assuming that delivery occurs exactly once?',
        'A strong answer assumes at least once delivery, uses stable event identifiers and durable deduplication where appropriate, and defines transaction boundaries for recording processing state and applying business changes. It also considers idempotent operations, retry behaviour, concurrent consumers and recovery after partial failure.'
) AS q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Java Advanced Engineering'
  AND c.name = 'Java'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

-- =========================================================
-- 3. MAP QUESTIONS TO EXISTING CONCEPTS
-- =========================================================

INSERT INTO interview_question_concepts
(question_id, concept_id)
SELECT
    iq.id,
    co.id
FROM interview_questions iq
JOIN interviews i
    ON i.id = iq.interview_id
JOIN (
    SELECT
        'Explain how you would design a production grade Java service that must remain maintainable, reliable and responsive under heavy traffic.' AS question_text,
        'layered-architecture' AS concept_name

    UNION ALL
    SELECT
        'Explain how you would design a production grade Java service that must remain maintainable, reliable and responsive under heavy traffic.',
        'maintainability'

    UNION ALL
    SELECT
        'Explain how you would design a production grade Java service that must remain maintainable, reliable and responsive under heavy traffic.',
        'performance-trade-offs'

    UNION ALL
    SELECT
        'Explain how you would design a production grade Java service that must remain maintainable, reliable and responsive under heavy traffic.',
        'request-tracing'

    UNION ALL
    SELECT
        'Describe a complex Java performance or reliability issue you investigated. Explain how you identified the root cause, evaluated possible solutions and measured the outcome.',
        'performance-profiling'

    UNION ALL
    SELECT
        'Describe a complex Java performance or reliability issue you investigated. Explain how you identified the root cause, evaluated possible solutions and measured the outcome.',
        'maintainability'

    UNION ALL
    SELECT
        'Describe a complex Java performance or reliability issue you investigated. Explain how you identified the root cause, evaluated possible solutions and measured the outcome.',
        'error-handling'

    UNION ALL
    SELECT
        'Describe a complex Java performance or reliability issue you investigated. Explain how you identified the root cause, evaluated possible solutions and measured the outcome.',
        'data-consistency'

    UNION ALL
    SELECT
        'How would you structure a Java application that needs to support multiple database implementations while keeping business rules independent of persistence details?',
        'layered-architecture'

    UNION ALL
    SELECT
        'How would you structure a Java application that needs to support multiple database implementations while keeping business rules independent of persistence details?',
        'dependency-injection'

    UNION ALL
    SELECT
        'How would you structure a Java application that needs to support multiple database implementations while keeping business rules independent of persistence details?',
        'repository-pattern'

    UNION ALL
    SELECT
        'How would you structure a Java application that needs to support multiple database implementations while keeping business rules independent of persistence details?',
        'testability'

    UNION ALL
    SELECT
        'Explain how Java memory visibility and thread safety differ, and describe how volatile, synchronized and atomic classes address concurrency problems.',
        'concurrency'

    UNION ALL
    SELECT
        'Explain how Java memory visibility and thread safety differ, and describe how volatile, synchronized and atomic classes address concurrency problems.',
        'java-basics'

    UNION ALL
    SELECT
        'Explain how Java memory visibility and thread safety differ, and describe how volatile, synchronized and atomic classes address concurrency problems.',
        'maintainability'

    UNION ALL
    SELECT
        'How would you implement a reliable background processing service in Java that handles concurrent tasks, limits resource usage and supports graceful shutdown?',
        'concurrency'

    UNION ALL
    SELECT
        'How would you implement a reliable background processing service in Java that handles concurrent tasks, limits resource usage and supports graceful shutdown?',
        'message-processing'

    UNION ALL
    SELECT
        'How would you implement a reliable background processing service in Java that handles concurrent tasks, limits resource usage and supports graceful shutdown?',
        'error-handling'

    UNION ALL
    SELECT
        'How would you implement a reliable background processing service in Java that handles concurrent tasks, limits resource usage and supports graceful shutdown?',
        'performance-trade-offs'

    UNION ALL
    SELECT
        'A Java application experiences intermittent deadlocks after a new feature introduces several shared locks. How would you diagnose the issue and prevent it from recurring?',
        'concurrency'

    UNION ALL
    SELECT
        'A Java application experiences intermittent deadlocks after a new feature introduces several shared locks. How would you diagnose the issue and prevent it from recurring?',
        'maintainability'

    UNION ALL
    SELECT
        'A Java application experiences intermittent deadlocks after a new feature introduces several shared locks. How would you diagnose the issue and prevent it from recurring?',
        'performance-profiling'

    UNION ALL
    SELECT
        'A Java application experiences intermittent deadlocks after a new feature introduces several shared locks. How would you diagnose the issue and prevent it from recurring?',
        'security-risk-assessment'

    UNION ALL
    SELECT
        'A Java service must process millions of database records within a limited time window. How would you choose between parallel processing, batched queries and asynchronous processing?',
        'batch-processing'

    UNION ALL
    SELECT
        'A Java service must process millions of database records within a limited time window. How would you choose between parallel processing, batched queries and asynchronous processing?',
        'query-optimization'

    UNION ALL
    SELECT
        'A Java service must process millions of database records within a limited time window. How would you choose between parallel processing, batched queries and asynchronous processing?',
        'performance-trade-offs'

    UNION ALL
    SELECT
        'A Java service must process millions of database records within a limited time window. How would you choose between parallel processing, batched queries and asynchronous processing?',
        'concurrency'

    UNION ALL
    SELECT
        'A distributed Java application sometimes processes the same incoming event more than once when consumers restart. How would you design the processing flow to protect data consistency without assuming that delivery occurs exactly once?',
        'message-processing'

    UNION ALL
    SELECT
        'A distributed Java application sometimes processes the same incoming event more than once when consumers restart. How would you design the processing flow to protect data consistency without assuming that delivery occurs exactly once?',
        'idempotency'

    UNION ALL
    SELECT
        'A distributed Java application sometimes processes the same incoming event more than once when consumers restart. How would you design the processing flow to protect data consistency without assuming that delivery occurs exactly once?',
        'data-consistency'

    UNION ALL
    SELECT
        'A distributed Java application sometimes processes the same incoming event more than once when consumers restart. How would you design the processing flow to protect data consistency without assuming that delivery occurs exactly once?',
        'sql-transactions'
) AS mapping
    ON mapping.question_text = iq.text
JOIN concepts co
    ON co.name = mapping.concept_name
WHERE i.interview_name = 'Java Advanced Engineering'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_question_concepts existing
      WHERE existing.question_id = iq.id
        AND existing.concept_id = co.id
  );
































# =========================================================
# C# EASY INTERVIEW
# 1 interview | 6 questions
# 1 concept per question
# Admin ID: 1
# =========================================================

# =========================================================
# 1. CREATE INTERVIEW
# =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'C# Fundamentals Beginner',
    c.id,
    1,
    'An introductory C# interview covering core language concepts, dependency injection and basic application development.',
    'easy',
    TRUE,
    20
FROM interview_categories c
WHERE c.name = 'C#'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'C# Fundamentals Beginner'
        AND existing.category_id = c.id
  );

# =========================================================
# 2. INSERT SIX QUESTIONS
# =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'easy',
    q.question_text,
    q.answer_text,
    TRUE
FROM interviews i
JOIN interview_categories c
    ON c.id = i.category_id
CROSS JOIN (
    SELECT
        'Introduction' AS question_type,
        'Tell me about your experience learning or developing applications with C#.' AS question_text,
        'A good answer introduces the candidate, describes their C# learning or development experience, mentions a relevant project and explains their current goals.' AS answer_text

    UNION ALL

    SELECT
        'Definition',
        'What is C# and what is it commonly used for?',
        'C# is a strongly typed, object oriented programming language in the .NET ecosystem. It is commonly used for web applications, desktop software, APIs, cloud services and games.'

    UNION ALL

    SELECT
        'Definition',
        'What is the difference between a value type and a reference type in C#?',
        'Value type variables contain their values directly, while reference type variables hold references to objects. Examples of value types include int and bool, while class instances and arrays are reference types.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is dependency injection and why is it useful in a C# application?',
        'Dependency injection provides a class with the services it needs instead of requiring the class to create those services itself. It reduces coupling and improves testability and maintainability.'

    UNION ALL

    SELECT
        'Implementation',
        'How would you write a C# method that checks whether an integer is even?',
        'One valid implementation is bool IsEven(int number) { return number % 2 == 0; }. The modulo operator determines whether the number is divisible by two without a remainder.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between a class and an object in C#?',
        'A class defines the data and behaviour of a type. An object is an instance of a class created at runtime and can hold its own instance state.'
) AS q
    ON TRUE
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'C# Fundamentals Beginner'
  AND c.name = 'C#'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

# =========================================================
# 3. MAP EACH QUESTION TO ONE EXISTING CONCEPT
# =========================================================

INSERT INTO interview_question_concepts
(question_id, concept_id)
SELECT
    iq.id,
    co.id
FROM interview_questions iq
JOIN interviews i
    ON i.id = iq.interview_id
JOIN (
    SELECT
        'Tell me about your experience learning or developing applications with C#.' AS question_text,
        'csharp-basics' AS concept_name

    UNION ALL

    SELECT
        'What is C# and what is it commonly used for?',
        'csharp-basics'

    UNION ALL

    SELECT
        'What is the difference between a value type and a reference type in C#?',
        'csharp-basics'

    UNION ALL

    SELECT
        'What is dependency injection and why is it useful in a C# application?',
        'dependency-injection'

    UNION ALL

    SELECT
        'How would you write a C# method that checks whether an integer is even?',
        'csharp-basics'

    UNION ALL

    SELECT
        'What is the difference between a class and an object in C#?',
        'csharp-basics'
) AS mapping
    ON mapping.question_text = iq.text
JOIN concepts co
    ON co.name = mapping.concept_name
WHERE i.interview_name = 'C# Fundamentals Beginner'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_question_concepts existing
      WHERE existing.question_id = iq.id
        AND existing.concept_id = co.id
  );































# =========================================================
# C# MEDIUM INTERVIEW
# 1 interview | 7 questions
# Admin ID: 1
# =========================================================

# 1. CREATE INTERVIEW

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'C# Intermediate Development',
    c.id,
    1,
    'An intermediate C# interview covering dependency injection, asynchronous programming, database access and practical problem solving.',
    'medium',
    TRUE,
    30
FROM interview_categories c
WHERE c.name = 'C#'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'C# Intermediate Development'
        AND existing.category_id = c.id
  );

# 2. INSERT QUESTIONS

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'medium',
    q.question_text,
    q.answer_text,
    TRUE
FROM interviews i
JOIN interview_categories c
    ON c.id = i.category_id
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Describe a C# application you have worked on and explain how its main components interact.' AS question_text,
        'A good answer explains the application purpose, its main components, how data moves between them and the candidates own contribution.' AS answer_text

    UNION ALL

    SELECT
        'Experience',
        'Describe a situation where you improved the performance or maintainability of a C# application. How did you evaluate the result?',
        'A strong answer describes the original problem, evidence used to identify the cause, changes made and how the result was measured. It should also mention testing and possible trade offs.'

    UNION ALL

    SELECT
        'Definition',
        'What is dependency injection in C# and how does constructor injection differ from creating dependencies inside a class?',
        'Dependency injection supplies required services from outside a class. Constructor injection makes those dependencies explicit, reduces tight coupling and improves testability compared with creating concrete dependencies internally.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between IEnumerable and IQueryable in C# when working with database data?',
        'IEnumerable supports iteration over a sequence and commonly performs subsequent operations in application memory. IQueryable builds expression trees that a compatible provider can translate, often into database queries. Execution depends on the provider and where the query is evaluated.'

    UNION ALL

    SELECT
        'Implementation',
        'How would you call an external API asynchronously in C# and handle a failed HTTP response?',
        'A good implementation uses HttpClient with await and an asynchronous request method. It checks the response status, uses EnsureSuccessStatusCode or equivalent handling, catches relevant exceptions and applies suitable cancellation and timeout behaviour.'

    UNION ALL

    SELECT
        'Debugging',
        'An ASP.NET Core endpoint becomes slow when it loads a list of records and related data. How would you find the cause and improve performance?',
        'A strong answer measures request duration, inspects generated SQL and execution plans, checks for excessive queries such as the N plus one problem and reviews indexes and selected columns. It then tests an appropriate query or loading strategy and compares performance under realistic conditions.'

    UNION ALL

    SELECT
        'Trade-off',
        'A C# application repeatedly requests the same data from a remote API. How would you decide whether to use caching, reduce duplicate requests or improve the API client design?',
        'A strong answer examines request frequency, data freshness requirements, latency and rate limits. It considers suitable caching, request deduplication, cancellation and error handling, while accounting for cache invalidation and consistency.'
) AS q
    ON 1 = 1
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'C# Intermediate Development'
  AND c.name = 'C#'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

# 3. MAP QUESTIONS TO EXISTING CONCEPTS

INSERT INTO interview_question_concepts
(question_id, concept_id)
SELECT
    iq.id,
    co.id
FROM interview_questions iq
JOIN interviews i
    ON i.id = iq.interview_id
JOIN (
    SELECT
        'Describe a C# application you have worked on and explain how its main components interact.' AS question_text,
        'csharp-basics' AS concept_name

    UNION ALL
    SELECT
        'Describe a C# application you have worked on and explain how its main components interact.',
        'layered-architecture'

    UNION ALL
    SELECT
        'Describe a situation where you improved the performance or maintainability of a C# application. How did you evaluate the result?',
        'maintainability'

    UNION ALL
    SELECT
        'Describe a situation where you improved the performance or maintainability of a C# application. How did you evaluate the result?',
        'performance-profiling'

    UNION ALL
    SELECT
        'What is dependency injection in C# and how does constructor injection differ from creating dependencies inside a class?',
        'dependency-injection'

    UNION ALL
    SELECT
        'What is dependency injection in C# and how does constructor injection differ from creating dependencies inside a class?',
        'constructor-injection'

    UNION ALL
    SELECT
        'What is the difference between IEnumerable and IQueryable in C# when working with database data?',
        'ienumerable'

    UNION ALL
    SELECT
        'What is the difference between IEnumerable and IQueryable in C# when working with database data?',
        'iqueryable'

    UNION ALL
    SELECT
        'What is the difference between IEnumerable and IQueryable in C# when working with database data?',
        'deferred-execution'

    UNION ALL
    SELECT
        'How would you call an external API asynchronously in C# and handle a failed HTTP response?',
        'async-await'

    UNION ALL
    SELECT
        'How would you call an external API asynchronously in C# and handle a failed HTTP response?',
        'http-client'

    UNION ALL
    SELECT
        'How would you call an external API asynchronously in C# and handle a failed HTTP response?',
        'error-handling'

    UNION ALL
    SELECT
        'An ASP.NET Core endpoint becomes slow when it loads a list of records and related data. How would you find the cause and improve performance?',
        'entity-framework'

    UNION ALL
    SELECT
        'An ASP.NET Core endpoint becomes slow when it loads a list of records and related data. How would you find the cause and improve performance?',
        'n-plus-one'

    UNION ALL
    SELECT
        'An ASP.NET Core endpoint becomes slow when it loads a list of records and related data. How would you find the cause and improve performance?',
        'query-optimization'

    UNION ALL
    SELECT
        'A C# application repeatedly requests the same data from a remote API. How would you decide whether to use caching, reduce duplicate requests or improve the API client design?',
        'api-caching'

    UNION ALL
    SELECT
        'A C# application repeatedly requests the same data from a remote API. How would you decide whether to use caching, reduce duplicate requests or improve the API client design?',
        'duplicate-requests'

    UNION ALL
    SELECT
        'A C# application repeatedly requests the same data from a remote API. How would you decide whether to use caching, reduce duplicate requests or improve the API client design?',
        'api-client'
) AS mapping
    ON mapping.question_text = iq.text
JOIN concepts co
    ON co.name = mapping.concept_name
WHERE i.interview_name = 'C# Intermediate Development'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_question_concepts existing
      WHERE existing.question_id = iq.id
        AND existing.concept_id = co.id
  );

























  # =========================================================
# C# HARD INTERVIEW
# 1 interview | 8 questions
# 3 to 4 concepts per question
# Admin ID: 1
# =========================================================

# =========================================================
# 1. CREATE INTERVIEW
# =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'C# Advanced Engineering',
    c.id,
    1,
    'An advanced C# interview covering application architecture, concurrency, API reliability, database performance and complex problem solving.',
    'hard',
    TRUE,
    45
FROM interview_categories c
WHERE c.name = 'C#'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'C# Advanced Engineering'
        AND existing.category_id = c.id
  );

# =========================================================
# 2. INSERT EIGHT QUESTIONS
# =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'hard',
    q.question_text,
    q.answer_text,
    TRUE
FROM interviews i
JOIN interview_categories c
    ON c.id = i.category_id
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Explain how you would design a production grade ASP.NET Core application that remains maintainable and responsive under heavy traffic.' AS question_text,
        'A strong answer discusses layered architecture, dependency injection, efficient database access, caching, observability, resilience and performance testing. The candidate should explain trade offs and how the design would be monitored in production.' AS answer_text

    UNION ALL

    SELECT
        'Experience',
        'Describe a difficult C# production issue you investigated. How did you identify the root cause, choose a solution and verify that the issue was resolved?',
        'A strong answer explains the symptoms, evidence collected, root cause, alternative solutions, implementation and measurable outcome. It should cover regression testing and any remaining operational risks.'

    UNION ALL

    SELECT
        'Architecture',
        'How would you structure a C# application so that business rules remain independent of database technology and external services?',
        'A strong answer separates domain logic from infrastructure, defines suitable interfaces, applies dependency inversion and injects implementations. It also considers transaction boundaries, testing, maintainability and whether each abstraction provides real value.'

    UNION ALL

    SELECT
        'Conceptual',
        'Explain how async and await affect execution in C# and why asynchronous code does not automatically make CPU intensive work faster.',
        'Async and await allow asynchronous operations to be composed without blocking a thread while waiting for supported operations to complete. They are especially useful for I O bound work. CPU intensive work still consumes processing capacity and may require measured parallel execution. The answer should address exception propagation, cancellation and resource limits.'

    UNION ALL

    SELECT
        'Implementation',
        'How would you implement a reliable background job processor in C# that limits concurrent work, handles failures and shuts down safely?',
        'A strong solution uses a bounded queue or suitable channel, controlled worker concurrency, cancellation tokens, explicit exception handling and graceful shutdown. It should also discuss retry limits, poison messages, resource cleanup, back pressure and operational monitoring.'

    UNION ALL

    SELECT
        'Debugging',
        'An ASP.NET Core API experiences intermittent timeouts under load even though individual database queries appear fast. How would you investigate and resolve the issue?',
        'A strong answer correlates request traces, application logs, dependency timings, thread pool activity and connection pool usage. It investigates blocking calls, resource exhaustion, excessive concurrency and downstream latency. Improvements should be validated with load tests and measurements rather than assumptions.'

    UNION ALL

    SELECT
        'Trade-off',
        'A C# service must retrieve and process millions of database records within a fixed time window. How would you choose between loading all records, using pagination or processing batches?',
        'A strong answer compares memory use, query cost, round trips, consistency, transaction duration and recovery requirements. Pagination or batching can control resource consumption, while streaming may reduce memory pressure. The design should account for ordering, concurrent updates and realistic performance measurements.'

    UNION ALL

    SELECT
        'Debugging',
        'A distributed application sometimes processes the same event more than once after a consumer restarts. How would you prevent duplicate business operations while preserving reliable message delivery?',
        'A strong answer assumes that message delivery can repeat. It uses stable event identifiers, durable deduplication, idempotent business operations and appropriate transaction boundaries. It also addresses concurrent consumers, retry policies and recovery after partial failure.'
) AS q
    ON 1 = 1
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'C# Advanced Engineering'
  AND c.name = 'C#'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

# =========================================================
# 3. MAP QUESTIONS TO EXISTING CONCEPTS
# Each question receives 3 or 4 concepts
# =========================================================

INSERT INTO interview_question_concepts
(question_id, concept_id)
SELECT
    iq.id,
    co.id
FROM interview_questions iq
JOIN interviews i
    ON i.id = iq.interview_id
JOIN (
    SELECT
        'Explain how you would design a production grade ASP.NET Core application that remains maintainable and responsive under heavy traffic.' AS question_text,
        'layered-architecture' AS concept_name

    UNION ALL
    SELECT
        'Explain how you would design a production grade ASP.NET Core application that remains maintainable and responsive under heavy traffic.',
        'dependency-injection'

    UNION ALL
    SELECT
        'Explain how you would design a production grade ASP.NET Core application that remains maintainable and responsive under heavy traffic.',
        'performance-profiling'

    UNION ALL
    SELECT
        'Explain how you would design a production grade ASP.NET Core application that remains maintainable and responsive under heavy traffic.',
        'request-tracing'

    UNION ALL
    SELECT
        'Describe a difficult C# production issue you investigated. How did you identify the root cause, choose a solution and verify that the issue was resolved?',
        'error-handling'

    UNION ALL
    SELECT
        'Describe a difficult C# production issue you investigated. How did you identify the root cause, choose a solution and verify that the issue was resolved?',
        'performance-profiling'

    UNION ALL
    SELECT
        'Describe a difficult C# production issue you investigated. How did you identify the root cause, choose a solution and verify that the issue was resolved?',
        'network-debugging'

    UNION ALL
    SELECT
        'How would you structure a C# application so that business rules remain independent of database technology and external services?',
        'layered-architecture'

    UNION ALL
    SELECT
        'How would you structure a C# application so that business rules remain independent of database technology and external services?',
        'dependency-injection'

    UNION ALL
    SELECT
        'How would you structure a C# application so that business rules remain independent of database technology and external services?',
        'loose-coupling'

    UNION ALL
    SELECT
        'How would you structure a C# application so that business rules remain independent of database technology and external services?',
        'testability'

    UNION ALL
    SELECT
        'Explain how async and await affect execution in C# and why asynchronous code does not automatically make CPU intensive work faster.',
        'async-await'

    UNION ALL
    SELECT
        'Explain how async and await affect execution in C# and why asynchronous code does not automatically make CPU intensive work faster.',
        'concurrency'

    UNION ALL
    SELECT
        'Explain how async and await affect execution in C# and why asynchronous code does not automatically make CPU intensive work faster.',
        'network-performance'

    UNION ALL
    SELECT
        'How would you implement a reliable background job processor in C# that limits concurrent work, handles failures and shuts down safely?',
        'async-await'

    UNION ALL
    SELECT
        'How would you implement a reliable background job processor in C# that limits concurrent work, handles failures and shuts down safely?',
        'message-processing'

    UNION ALL
    SELECT
        'How would you implement a reliable background job processor in C# that limits concurrent work, handles failures and shuts down safely?',
        'error-handling'

    UNION ALL
    SELECT
        'How would you implement a reliable background job processor in C# that limits concurrent work, handles failures and shuts down safely?',
        'concurrency'

    UNION ALL
    SELECT
        'An ASP.NET Core API experiences intermittent timeouts under load even though individual database queries appear fast. How would you investigate and resolve the issue?',
        'request-tracing'

    UNION ALL
    SELECT
        'An ASP.NET Core API experiences intermittent timeouts under load even though individual database queries appear fast. How would you investigate and resolve the issue?',
        'performance-profiling'

    UNION ALL
    SELECT
        'An ASP.NET Core API experiences intermittent timeouts under load even though individual database queries appear fast. How would you investigate and resolve the issue?',
        'network-performance'

    UNION ALL
    SELECT
        'An ASP.NET Core API experiences intermittent timeouts under load even though individual database queries appear fast. How would you investigate and resolve the issue?',
        'query-optimization'

    UNION ALL
    SELECT
        'A C# service must retrieve and process millions of database records within a fixed time window. How would you choose between loading all records, using pagination or processing batches?',
        'batch-processing'

    UNION ALL
    SELECT
        'A C# service must retrieve and process millions of database records within a fixed time window. How would you choose between loading all records, using pagination or processing batches?',
        'query-optimization'

    UNION ALL
    SELECT
        'A C# service must retrieve and process millions of database records within a fixed time window. How would you choose between loading all records, using pagination or processing batches?',
        'performance-trade-offs'

    UNION ALL
    SELECT
        'A C# service must retrieve and process millions of database records within a fixed time window. How would you choose between loading all records, using pagination or processing batches?',
        'data-consistency'

    UNION ALL
    SELECT
        'A distributed application sometimes processes the same event more than once after a consumer restarts. How would you prevent duplicate business operations while preserving reliable message delivery?',
        'message-processing'

    UNION ALL
    SELECT
        'A distributed application sometimes processes the same event more than once after a consumer restarts. How would you prevent duplicate business operations while preserving reliable message delivery?',
        'idempotency'

    UNION ALL
    SELECT
        'A distributed application sometimes processes the same event more than once after a consumer restarts. How would you prevent duplicate business operations while preserving reliable message delivery?',
        'data-consistency'

    UNION ALL
    SELECT
        'A distributed application sometimes processes the same event more than once after a consumer restarts. How would you prevent duplicate business operations while preserving reliable message delivery?',
        'sql-transactions'
) AS mapping
    ON mapping.question_text = iq.text
JOIN concepts co
    ON co.name = mapping.concept_name
WHERE i.interview_name = 'C# Advanced Engineering'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_question_concepts existing
      WHERE existing.question_id = iq.id
        AND existing.concept_id = co.id
  );

























  # =========================================================
# CYBERSECURITY EASY INTERVIEW
# 1 interview | 6 questions
# 1 concept per question
# Admin ID: 1
# =========================================================

# =========================================================
# 1. CREATE INTERVIEW
# =========================================================

INSERT INTO interviews
(interview_name, category_id, admin_id, description,
 difficulty, active, estimated_time_in_minutes)
SELECT
    'Cybersecurity Fundamentals Beginner',
    c.id,
    1,
    'An introductory cybersecurity interview covering security principles, common threats, access control and basic defensive practices.',
    'easy',
    TRUE,
    20
FROM interview_categories c
WHERE c.name = 'Cybersecurity'
  AND NOT EXISTS (
      SELECT 1
      FROM interviews existing
      WHERE existing.interview_name = 'Cybersecurity Fundamentals Beginner'
        AND existing.category_id = c.id
  );

# =========================================================
# 2. INSERT SIX QUESTIONS
# =========================================================

INSERT INTO interview_questions
(interview_id, admin_id, type_id, difficulty, text, answer, active)
SELECT
    i.id,
    1,
    qt.id,
    'easy',
    q.question_text,
    q.answer_text,
    TRUE
FROM interviews i
JOIN interview_categories c
    ON c.id = i.category_id
JOIN (
    SELECT
        'Introduction' AS question_type,
        'Introduce yourself and explain why cybersecurity interests you.' AS question_text,
        'A good answer introduces the candidate, explains their interest in protecting systems and data, and mentions relevant learning, projects or security goals.' AS answer_text

    UNION ALL

    SELECT
        'Definition',
        'What is cybersecurity and what is its main purpose?',
        'Cybersecurity protects systems, networks, applications and data against unauthorised access, attacks, damage and disruption.'

    UNION ALL

    SELECT
        'Definition',
        'What is the CIA triad in cybersecurity?',
        'The CIA triad consists of confidentiality, integrity and availability. Confidentiality limits access to authorised parties, integrity protects the accuracy of data, and availability ensures systems and data can be accessed when needed.'

    UNION ALL

    SELECT
        'Conceptual',
        'What is the difference between authentication and authorization?',
        'Authentication verifies who a user or system is. Authorization determines which resources or actions that authenticated identity is permitted to access.'

    UNION ALL

    SELECT
        'Conceptual',
        'Why is the principle of least privilege important when assigning user permissions?',
        'Least privilege gives each user or service only the permissions required for its tasks. This reduces the impact of compromised accounts, mistakes and unauthorised actions.'

    UNION ALL

    SELECT
        'Implementation',
        'What basic steps would you take to secure an account against unauthorised access?',
        'A good answer includes using a strong unique password, enabling multi factor authentication, limiting login attempts where appropriate, protecting recovery methods and monitoring suspicious activity.'
) AS q
    ON 1 = 1
JOIN question_types qt
    ON qt.name = q.question_type
WHERE i.interview_name = 'Cybersecurity Fundamentals Beginner'
  AND c.name = 'Cybersecurity'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_questions existing
      WHERE existing.interview_id = i.id
        AND existing.text = q.question_text
  );

# =========================================================
# 3. MAP EACH QUESTION TO ONE EXISTING CONCEPT
# =========================================================

INSERT INTO interview_question_concepts
(question_id, concept_id)
SELECT
    iq.id,
    co.id
FROM interview_questions iq
JOIN interviews i
    ON i.id = iq.interview_id
JOIN (
    SELECT
        'Introduce yourself and explain why cybersecurity interests you.' AS question_text,
        'cybersecurity-basics' AS concept_name

    UNION ALL

    SELECT
        'What is cybersecurity and what is its main purpose?',
        'cybersecurity-basics'

    UNION ALL

    SELECT
        'What is the CIA triad in cybersecurity?',
        'cia-triad'

    UNION ALL

    SELECT
        'What is the difference between authentication and authorization?',
        'authentication'

    UNION ALL

    SELECT
        'Why is the principle of least privilege important when assigning user permissions?',
        'least-privilege'

    UNION ALL

    SELECT
        'What basic steps would you take to secure an account against unauthorised access?',
        'multi-factor-authentication'
) AS mapping
    ON mapping.question_text = iq.text
JOIN concepts co
    ON co.name = mapping.concept_name
WHERE i.interview_name = 'Cybersecurity Fundamentals Beginner'
  AND NOT EXISTS (
      SELECT 1
      FROM interview_question_concepts existing
      WHERE existing.question_id = iq.id
        AND existing.concept_id = co.id
  );


















 