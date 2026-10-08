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

            $validQuestions = $this->db->queryAll($query,array_merge([$interviewId], $questionIds));
            if (count($validQuestions) !== count($questionIds)) {
                throw new ValidationException('One or more questions do not belong to this interview.');
            }


            $this->db->transaction(function() use ($userId,$interviewId,$answers){
                $query = "INSERT INTO interview_user_attempts (user_id,interview_id) VALUES (?,?)";
                $this->db->execute($query,[$userId,$interviewId]);
                $user_attempt_id = $this->db->lastInsertId();
                $placeHolders = [];
                $params = [];

                foreach($answers as $answer){
                    $placeHolders[] = "(?,?,?,?)";
                    $params[] = $user_attempt_id;
                    $params[] = $answer['id'];
                    $params[] = $answer['answer'];
                    $params[] = $answer['secondsSpent'];
                }
                $query = "INSERT INTO interview_question_attempts (user_attempt_id,question_id,answer,seconds_spent) VALUES " . implode(',',$placeHolders);
                $this->db->execute($query,$params);
            });
            
        }

        public function getAttempt(int $attemptId):array{
            return $this->db->queryOne("SELECT * FROM interview_user_attempts WHERE interview_id=?",[$attemptId]);
        }

        public function getQuestionsForAnalysis(int $attemptId): array{
            $query = "SELECT iq.id,iq.answer as expected_answer,iq.text as question,iqa.answer,iqa.id as question_attempt_id
                FROM interview_questions iq 
                JOIN interview_question_attempts iqa ON iq.id = iqa.question_id 
                WHERE interview_id =?";
            return $this->db->queryAll($query,[$attemptId]);
        }

        public function addAnalyseQuestionConcepts($aiQuestionsConcepts){
            $placeHolders = [];
            $params = [];
            //return $aiQuestionsConcepts;
            foreach($aiQuestionsConcepts as $concepts){
                $placeHolders[] = "(?,?,?)";
                $params[] = $concepts['question_attempt_id'];
                $params[] = $concepts['concept_id'];
                $params[] = $concepts['score'];
            }

            $query = 'INSERT INTO question_concept_scores (question_attempt_id,concept_id,score) VALUES ' . implode(',',$placeHolders);
            $this->db->execute($query,$params);
        }

        public function addAnalyseInterview($aiInterviewCompetencies){
            $placeHolders = [];
            $params = [];
            //return $aiInterviewCompetencies;

            foreach($aiInterviewCompetencies as $interviewCompetencies){
                $placeHolders[] = "(?,?,?,?)";
                $params[] = $interviewCompetencies['interview_attempt_id'];
                $params[] = $interviewCompetencies['competency_breakdown_id'];
                $params[] = $interviewCompetencies['strength'];
                $params[] = $interviewCompetencies['improvement'];
            }

            $query = 'INSERT INTO interview_competency_breakdown_feedbacks (interview_attempt_id,competency_breakdown_id,strength,improvement) VALUES ' . implode(',',$placeHolders);
            $this->db->execute($query,$params);
        }

        public function addAnalyseQuestionCompetencies($aiQuestionCompetencies){
            $placeHolders = [];
            $params = [];
            //return $aiQuestionCompetencies;
            foreach($aiQuestionCompetencies as $competencies){
                $placeHolders[] = "(?,?,?)";
                $params[] = $competencies['question_attempt_id'];
                $params[] = $competencies['competency_breakdown_id'];
                $params[] = $competencies['score'];
            }
            
            $query = 'INSERT INTO question_competency_breakdown_scores (question_attempt_id,competency_breakdown_id,score) VALUES ' . implode(',',$placeHolders);
            $this->db->execute($query,$params);
        }
    }
?>