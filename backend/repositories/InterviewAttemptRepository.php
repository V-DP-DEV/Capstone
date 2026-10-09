<?php
    class InterviewAttemptRepository implements IInterviewAttemptRepository{
        public function __construct(private Db $db){}

        public function submitAttempt($userId,$interviewId, $answers){
            //checks any dupe ids
            $questionIds = array_column($answers, 'id');
            if (count($questionIds) !== count(array_unique($questionIds))) {
                throw new ValidationException('Duplicate questions submitted.');
            }

            //checks if questions belong to interview
            $placeholders = implode(',', array_fill(0, count($questionIds), '?'));
            $query = "
                SELECT id
                FROM interview_questions
                WHERE interview_id = ?
                AND id IN ($placeholders)
            ";

            //gets the questions from db and compares count
            $validQuestions = $this->db->queryAll($query,array_merge([$interviewId], $questionIds));
            if (count($validQuestions) !== count($questionIds)) {
                throw new ValidationException('One or more questions do not belong to this interview.');
            }

            //if passed start transaction 
            $this->db->transaction(function() use ($userId,$interviewId,$answers){
                //create the user interview attempt
                $query = "INSERT INTO interview_user_attempts (user_id,interview_id) VALUES (?,?)";
                $this->db->execute($query,[$userId,$interviewId]);
                $attemptId = $this->db->lastInsertId();

                //add the question responses
                $user_attempt_id = $this->db->lastInsertId();
                
                //build the params and placeholders to be passed
                $placeHolders = [];
                $params = [];
                foreach($answers as $answer){
                    $placeHolders[] = "(?,?,?,?)";
                    $params[] = $user_attempt_id;
                    $params[] = $answer['id'];
                    $params[] = $answer['answer'];
                    $params[] = $answer['secondsSpent'];
                }
                //insert into the db
                $query = "INSERT INTO interview_question_attempts (interview_attempt_id,question_id,answer,seconds_spent) VALUES " . implode(',',$placeHolders);
                $this->db->execute($query,$params);
                return $attemptId;
            });
            
        }

        //gets the users attempt and returns all information, without questions
        public function getAttempt(int $attemptId):array{
            return $this->db->queryOne("SELECT * FROM interview_user_attempts WHERE id=?",[$attemptId]);
        }

        //get the question answer, expected answer and question for analysis
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

            //performs the query
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

            //performs the query
            $query = 'INSERT INTO interview_competency_breakdown_feedbacks (interview_attempt_id,competency_breakdown_id,strength,improvement) VALUES ' . implode(',',$placeHolders);
            $this->db->execute($query,$params);
        }

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
            
            //performs the query
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
    }
?>