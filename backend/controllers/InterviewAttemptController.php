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
            //extract id
            $data = Request::json();
            $attemptId = $data["id"];
            
            //attempt analysis
            $this->interviewAnalysisService->analysis($attemptId);
            //returns result, and other errors will be thrown to error exception handler
            JsonResponse::success();
        }

        public function getAttempt(array $filters){
            //requires get
            Request::requireMethod("GET");
            //extracts the id from filters
            $id = $filters['id'] ?? null;
            //check if id not null
            if($id === null){
                throw new ValidationException("Missing required parameter: id");
            }
            //attempts to return the attempt data
            JsonResponse::success($this->interviewAttemptService->getAttempt($id));
            //return json
        }

        public function getAttempts(array $filters){
            //requires get
            Request::requireMethod("GET");
            //extracts filters, sort filters will be checked etc on service and repo
            $sortBy = $filters['sortBy'] ?? null;
            $sortOrder = $filters['sortOrder'] ?? null;
            //try to get the attempts
            JsonResponse::success($this->interviewAttemptService->getAttempts($sortBy,$sortOrder));
        }

        public function deleteAttempt(){
            //requires method delete
            Request::requireMethod("DELETE");
            //extract attempt id
            $data = Request::json();
            $attemptId =$data['id'];
            //delete the attempt
            $this->interviewAttemptService->deleteAttempt($attemptId);
            //returns success
            JsonResponse::success();
            //call service
        }

        public function deleteAttempts(){
            //requires method delete
            Request::requireMethod("DELETE");
            //tries to delete ann
            $this->interviewAttemptService->deleteAttempts();
            //returns success
            JsonResponse::success();
        }
    }
?>