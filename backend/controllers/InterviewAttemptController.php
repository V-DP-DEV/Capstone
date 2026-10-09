<?php
    class InterviewAttemptController{
        public function __construct(private InterviewAttemptService $interviewAttemptService,private RequestContext $requestContext,private InterviewEvaluationBuilder $evaluationBuilder,private OpenAIService $openAiService,private InterviewAnalysisService $interviewAnalysisService){} 

        public function submitAttempt(){
            
            //requries POST
            Request::requireMethod("POST");
            //gets the data from the body
            $data = Request::json();
            //gets the id
            $interviewId = $data["id"];
            //gets the answer
            $answers = $data["answers"];

            //passes it to the submit attempt service
            $attemptId = $this->interviewAttemptService->submitAttempt($interviewId,$answers);
            JsonResponse::success(['interviewAttemptId'=>$attemptId]);
        }

        public function analyse(){
            //requires post
            Request::requireMethod("POST");
            $data = Request::json();
            $attemptId = $data["attemptId"];
            
            $interviewId = $this->interviewAnalysisService->analysis($attemptId);
            //returns result, and other errors will be thrown to error exception handler
            JsonResponse::success(['attemptId'=>$interviewId]);
        }
    }
?>