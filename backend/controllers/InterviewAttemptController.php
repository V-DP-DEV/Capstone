<?php
    class InterviewAttemptController{
        public function __construct(private InterviewAttemptService $interviewAttemptService,private RequestContext $requestContext){} 

        public function submitAttempt(){
            
            
            Request::requireMethod("POST");
            $data = Request::json();
            $interviewId = $data["id"];
            $answers = $data["answers"];


            $result = $this->interviewAttemptService->submitAttempt($interviewId,$answers);
            JsonResponse::success();
            
        }
    }
?>