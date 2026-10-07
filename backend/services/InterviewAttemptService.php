<?php
    class InterviewAttemptService{
        public function __construct(private Db $db,private RequestContext $request_context, private IInterviewAttemptRepository $interviewAttemptRepository){

        }

        public function submitAttempt($interviewId,$answers){
     
            return $this->interviewAttemptRepository->submitAttempt($this->request_context->getUserId(),$interviewId,$answers);
        }
    }
?>