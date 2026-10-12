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
            $attemptId = $data["id"];
            
            $interviewId = $this->interviewAnalysisService->analysis($attemptId);
            //returns result, and other errors will be thrown to error exception handler
            JsonResponse::success();
        }

        public function getAttempt(array $filters){
            Request::requireMethod("GET");
            $id = $filters['id'] ?? null;
            if($id === null){
                throw new ValidationException("Missing required parameter: id");
            }
            JsonResponse::success($this->interviewAttemptService->getAttempt($id));
            //return json
        }

        public function getAttempts(array $filters){
            Request::requireMethod("GET");
            $sortBy = $filters['sortBy'] ?? null;
            $sortOrder = $filters['sortOrder'] ?? null;
            JsonResponse::success($this->interviewAttemptService->getAttempts($sortBy,$sortOrder));
        }

        public function deleteAttempt(){
            Request::requireMethod("DELETE");
            $data = Request::json();
            $attemptId =$data['id'];
            $this->interviewAttemptService->deleteAttempt($attemptId);
            //call service
        }

        public function deleteAttempts(){
            Request::requireMethod("DELETE");
            $this->interviewAttemptService->deleteAttempts();
            //call service
        }
    }
?>