<?php
    class InterviewAttemptController{
        public function __construct(private InterviewAttemptService $interviewAttemptService,private RequestContext $requestContext,private InterviewEvaluationBuilder $evaluationBuilder,private OpenAIService $openAiService,private InterviewAnalysisService $interviewAnalysisService){} 

        public function submitAttempt(){
            
            
            Request::requireMethod("POST");
            $data = Request::json();
            $interviewId = $data["id"];
            $answers = $data["answers"];


            $result = $this->interviewAttemptService->submitAttempt($interviewId,$answers);
            JsonResponse::success();
        }

        public function analyse(){
            Request::requireMethod("POST");
            $data = Request::json();
            //$attemptId = $data["attemptId"];

            $response = $this->interviewAnalysisService->analysis(1);
            JsonResponse::success($response);
        }
    }
?>