<?php
    class InterviewAttemptRepository implements IInterviewAttemptRepository{
        public function __construct(private Db $db){}

        public function submitAttempt($interviewId, $answers){
            //$this->db->transaction(function use  );
            
        }
    }
?>