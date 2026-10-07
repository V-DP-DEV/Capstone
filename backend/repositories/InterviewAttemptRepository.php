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
    }
?>