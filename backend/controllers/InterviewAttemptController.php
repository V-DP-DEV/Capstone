<?php
    class InterviewAttemptController{
        public function __construct(private InterviewAttemptService $interviewAttemptService,private RequestContext $requestContext,private InterviewEvaluationBuilder $evaluationBuilder,private OpenAIService $openAiService){} 

        public function submitAttempt(){
            
            
            Request::requireMethod("POST");
            $data = Request::json();
            $interviewId = $data["id"];
            $answers = $data["answers"];


            $result = $this->interviewAttemptService->submitAttempt($interviewId,$answers);
            JsonResponse::success();
        }

        public function analyse(){
            $body = $this->evaluationBuilder->build([],[]);

            $response = $this->openAiService->createResponse($body);
            JsonResponse::success($response);
        }
    }
?>