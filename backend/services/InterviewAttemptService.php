<?php
    class InterviewAttemptService{
        public function __construct(private Db $db,private RequestContext $request_context, private IInterviewAttemptRepository $interviewAttemptRepository){

        }

        public function submitAttempt($interviewId,$answers){
            //TO:DO
            //do additional checks to check if attempt already made?
            //is being done by constraints
            return $this->interviewAttemptRepository->submitAttempt($this->request_context->getUserId(),$interviewId,$answers);
        }

        
    }
?>