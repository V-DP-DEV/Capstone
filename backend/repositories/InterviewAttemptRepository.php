<?php
    class InterviewAttemptRepository implements IInterviewAttemptRepository{
        public function __construct(private Db $db){}


        public function addInterviewUserAttempt(int $userId,int $interviewId):int{
            //create the user interview attempt
            $query = "INSERT INTO interview_user_attempts (user_id,interview_id) VALUES (?,?)";
            return $this->db->execute($query,[$userId,$interviewId]);
        }

        public function addInterviewUserAttemptQuestions(int $attemptId,array $answers):int{
            //build the params and placeholders to be passed
            $placeHolders = [];
            $params = [];
            foreach($answers as $answer){
                $placeHolders[] = "(?,?,?,?)";
                $params[] = $attemptId;
                $params[] = $answer['id'];
                $params[] = $answer['answer'];
                $params[] = $answer['secondsSpent'];
            }
            //insert into the db all question attempts once off
            $query = "INSERT INTO interview_question_attempts (interview_attempt_id,question_id,answer,seconds_spent) VALUES " . implode(',',$placeHolders);
            return $this->db->execute($query,$params);
        }

        public function getQuestionsMatching($questionIds,$interviewId){
            //checks if questions belong to interview
            $placeholders = implode(',', array_fill(0, count($questionIds), '?'));
            $query = "
                SELECT id
                FROM interview_questions
                WHERE interview_id = ?
                AND id IN ($placeholders)
            ";
            //returns all questions that matches
            return $this->db->queryAll($query,array_merge([$interviewId], $questionIds));
        }

        //gets the users attempt and returns all information, without questions
        public function getAttempt(int $attemptId):array{
            return $this->db->queryOne("SELECT * FROM interview_user_attempts WHERE id=?",[$attemptId]);
        }

        public function getAttemptDetails(int $attemptId):array{
            //coalesce used to ensure if null->0
            //case used to do bool check if completed in time
            //
            $query = "SELECT
                        iua.id as id,
                        i.interview_name as name,
                        i.difficulty as difficulty,
                        i.description as description,
                        i.id as interview_id,
                        iua.completed_at as completedAt,
                        iua.analysed as analysed,
                        iua.status as analysedStatus,
                        iua.reviewed as reviewed,
                        iua.user_id as userId,
                        i.estimated_time_in_minutes as estimatedMinutes,
                        
                        
                        COALESCE(iqa.totalSecondsSpent, 0) AS totalSecondsSpent,
                        CASE
                            WHEN COALESCE(iqa.totalSecondsSpent, 0) < (i.estimated_time_in_minutes*60)
                            THEN 1
                            ELSE 0
                        END AS completedInTime
                    FROM interview_user_attempts iua
                    LEFT JOIN (
                        SELECT
                            interview_attempt_id,
                            SUM(seconds_spent) AS totalSecondsSpent
                        FROM interview_question_attempts
                        GROUP BY interview_attempt_id
                    ) iqa
                    ON iqa.interview_attempt_id = iua.id
                    LEFT JOIN interviews i
                    ON iua.interview_id = i.id
                    WHERE iua.id = ?";
            return $this->db->queryOne($query,[$attemptId]);
        }

        //gets competency feedback with overall percentage
        //competency breakdown percentage
        //and strength + improvement
        public function getCompetenciesFeedback($attemptId){
            //with used to make query more readable
            //CTE 1 
            //breakdown scores gets percentage, competency breakdown name and competency
            //with strenght + improvement
            //group by used to correctly aggregate score to competency breakdown

            //CTE 2
            //calculates the total competency from cte1
            //group by used to correctly aggregate according to competency
            
            //last query gets all the results from cte1, cte2
            //and joins them
            $query = 'WITH breakdown_scores AS (
                SELECT
                    cb.competency_id,
                    icbf.competency_breakdown_id,
                    (AVG(qcbc.score) / 5) breakdown_percentage,
    				cb.name competency_breakdown_name,
    				c.name competency_name,
                    icbf.strength,
                    icbf.improvement
    				
                    FROM interview_competency_breakdown_feedbacks icbf
                    JOIN interview_question_attempts iqa
                        ON iqa.interview_attempt_id = icbf.interview_attempt_id
                    JOIN question_competency_breakdown_scores qcbc
                        ON qcbc.question_attempt_id = iqa.id
                    AND qcbc.competency_breakdown_id =
                        icbf.competency_breakdown_id
                    JOIN competency_breakdowns cb
                        ON cb.id = icbf.competency_breakdown_id
    			    JOIN competencies c
    				    ON c.id = cb.competency_id
                    WHERE icbf.interview_attempt_id = ?
                    GROUP BY
                        cb.competency_id,
                        icbf.competency_breakdown_id
                    ),
                competency_totals AS (
                    SELECT
                        competency_id,
                        (AVG(breakdown_percentage)) AS competency_percentage
                    FROM breakdown_scores
                    GROUP BY competency_id
                )
                SELECT
                bs.*,
                ct.competency_percentage
                FROM breakdown_scores bs
                JOIN competency_totals ct
                    ON ct.competency_id = bs.competency_id;';
            return $this->db->queryAll($query,[$attemptId]);
        }

        //gets question attempts
        //gets scores if there is any scores for competencies
        //gets scores if any for concepts but still returns concepts
        //the competency score will be the question score
        public function getAttemptQuestions(int $attemptId):array{
            //with and CTEs used for readibility
            //first cte calculates the score
            //groups to do it per question

            //final query uses CTE
            //gets relevant information about question
            //than attempts to join to get expected answer and question
            //joins to get question types
            //gets the concepts and if it has any scoring with it
            $query = "WITH competency_score AS (
                        SELECT
                            AVG(qcbc.score) AS competency_score,
                            iqa.question_id
                        FROM interview_question_attempts iqa
                        LEFT JOIN question_competency_breakdown_scores qcbc
                            ON qcbc.question_attempt_id = iqa.id
                        WHERE iqa.interview_attempt_id = ?
                        GROUP BY iqa.question_id
                        )
                        SELECT
                            
                            c.name AS concept_name,
                            c.id AS concept_id,
                            qcs.score AS concept_score,
                            iqa.seconds_spent,
                            iqa.question_id id,
                            iqa.answer,
                            qt.name 'type',
                            iq.answer AS expectedAnswer,
                            iqa.seconds_spent AS secondsSpent,
                            iq.text AS question,
                            iq.difficulty,
                            5 AS maxScore,
                            cs.competency_score as score
                        FROM interview_question_attempts iqa
                        JOIN interview_questions iq
                            ON iq.id = iqa.question_id
                        LEFT JOIN question_types qt
                        	ON qt.id = iq.type_id
                        LEFT JOIN interview_question_concepts iqc
                            ON iqc.question_id = iqa.question_id
                        LEFT JOIN concepts c
                            ON c.id = iqc.concept_id
                        LEFT JOIN question_concept_scores qcs
                            ON qcs.question_attempt_id = iqa.id
                            AND qcs.concept_id = iqc.concept_id
                        LEFT JOIN competency_score cs
                            ON iq.id = cs.question_id
                        WHERE iqa.interview_attempt_id = ?;";
            return $this->db->queryAll($query,[$attemptId,$attemptId]);
        }


        //gets high overall summary of attempts
        public function getAttempts(int $userId, SortOptions $sortOptions){
            //sort options maps field and throws error if not in field.
            //used for security
            $sort_collumns = [
                'completedAt' => 'iua.completed_at',
                'scorePercentage' => 'iua.score_percentage'
            ];

            $sortColumn = $sortOptions->mapField($sort_collumns);

            //query uses coalesce to make null ->0
            //uses case to check if completed in time
            
            //does sub query to get total seconds spent
            //sub query groups by interview id

            //joins interview attempt to interview to get other details
            //such as name difficulty and estimated time
            //aliased for frontend
            $query = "SELECT
                        iua.id as id,
                        i.interview_name as name,
                        i.difficulty as difficulty,
                        i.id as interview_id,
                        iua.completed_at as completedAt,
                        iua.analysed as analysed,
                        iua.status as analysedStatus,
                        i.estimated_time_in_minutes as estimatedMinutes,
                        iua.reviewed as reviewed,
                        
                        
                        COALESCE(iqa.totalSecondsSpent, 0) AS totalSecondsSpent,
                        CASE
                            WHEN COALESCE(iqa.totalSecondsSpent, 0) < (i.estimated_time_in_minutes*60)
                            THEN 1
                            ELSE 0
                        END AS completedInTime
                    FROM interview_user_attempts iua
                    LEFT JOIN (
                        SELECT
                            interview_attempt_id,
                            SUM(seconds_spent) AS totalSecondsSpent
                        FROM interview_question_attempts
                        GROUP BY interview_attempt_id
                    ) iqa
                    ON iqa.interview_attempt_id = iua.id
                    LEFT JOIN interviews i
                    ON iua.interview_id = i.id
                    WHERE iua.user_id = ?
                    ORDER BY " . $sortColumn . " ". $sortOptions->direction .";";
            return $this->db->queryAll($query,[$userId]);
        }

        //get the question answer, expected answer and question for analysis
        //needs to join question attempt with question
        public function getQuestionsForAnalysis(int $attemptId): array{
            $query = "SELECT iq.id,iq.answer as expected_answer,iq.text as question,iqa.answer,iqa.id as question_attempt_id
                FROM interview_questions iq 
                JOIN interview_question_attempts iqa ON iq.id = iqa.question_id 
                WHERE iqa.interview_attempt_id =? AND iq.active = 1";
            return $this->db->queryAll($query,[$attemptId]);
        }

        //adds the analysed question concepts for an attempt
        public function addAnalyseQuestionConcepts($aiQuestionsConcepts){
            //creates the params and placeholders to do one bulk query
            $placeHolders = [];
            $params = [];
            foreach($aiQuestionsConcepts as $concepts){
                $placeHolders[] = "(?,?,?)";
                $params[] = $concepts['question_attempt_id'];
                $params[] = $concepts['concept_id'];
                $params[] = $concepts['score'];
            }

            //performs the query for all questions
            $query = 'INSERT INTO question_concept_scores (question_attempt_id,concept_id,score) VALUES ' . implode(',',$placeHolders);
            $this->db->execute($query,$params);
        }

        //adds the analysed interview competencies
        public function addAnalyseInterview($aiInterviewCompetencies){
            //creates the params and placeholders to do one bulk query
            $placeHolders = [];
            $params = [];
           
            foreach($aiInterviewCompetencies as $interviewCompetencies){
                $placeHolders[] = "(?,?,?,?)";
                $params[] = $interviewCompetencies['interview_attempt_id'];
                $params[] = $interviewCompetencies['competency_breakdown_id'];
                $params[] = $interviewCompetencies['strength'];
                $params[] = $interviewCompetencies['improvement'];
            }

            //performs the query for all competencies
            $query = 'INSERT INTO interview_competency_breakdown_feedbacks (interview_attempt_id,competency_breakdown_id,strength,improvement) VALUES ' . implode(',',$placeHolders);
            $this->db->execute($query,$params);
        }

        //inserts data into question competencies analysed
        public function addAnalyseQuestionCompetencies($aiQuestionCompetencies){
            //creates the params and placeholders to do one bulk query
            $placeHolders = [];
            $params = [];

            foreach($aiQuestionCompetencies as $competencies){
                $placeHolders[] = "(?,?,?)";
                $params[] = $competencies['question_attempt_id'];
                $params[] = $competencies['competency_breakdown_id'];
                $params[] = $competencies['score'];
            }
            
            //performs the query for all questions
            $query = 'INSERT INTO question_competency_breakdown_scores (question_attempt_id,competency_breakdown_id,score) VALUES ' . implode(',',$placeHolders);
            $this->db->execute($query,$params);
        }

        public function updateAttemptToAnalysed($interviewAttemptId){
            $query = 'UPDATE interview_user_attempts SET analysed = 1 WHERE id=?';
            return $this->db->execute($query,[$interviewAttemptId]);
        }

        public function updateAnalyseStatus($interviewAttemptId,$status){
            $query = 'UPDATE interview_user_attempts SET status = ? WHERE id=?';
            return $this->db->execute($query,[$status,$interviewAttemptId]);
        }
        public function updateScorePercentage($interviewAttemptId,$percentage){
            $query = 'UPDATE interview_user_attempts SET score_percentage = ? WHERE id=?';
            return $this->db->execute($query,[$percentage,$interviewAttemptId]);
        }

        public function deleteAttempt($interviewAttemptId){
            $query = "DELETE FROM interview_user_attempts WHERE id = ?";
            return $this->db->execute($query,[$interviewAttemptId]);
        }

        public function deleteAttempts($userId){
            $query = "DELETE FROM interview_user_attempts WHERE user_id = ?";
            return $this->db->execute($query,[$userId]);
        }

        public function setToReviewed(int $attemptId):int{
            $query = "UPDATE interview_user_attempts SET reviewed = ? WHERE id=?";
            return $this->db->execute($query,[true,$attemptId]);
        }
    }
?>