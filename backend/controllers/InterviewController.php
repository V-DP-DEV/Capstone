<?php
    class InterviewController{
        public function __construct(private InterviewService $interviewService){}

        public function getAllInterviewCategories(){
            //requires method get
            Request::requireMethod("GET");
            //tries to get the data
            $result = $this->interviewService->getAllInterviewCategories();
            //returns result, and other errors will be thrown to error exception handler
            JsonResponse::success($result);
        }

        public function getInterview(array $filters){
            //requires method get
            Request::requireMethod("GET");
            //extracts the id
            $id = $filters['id'] ?? null;
            if($id === null){
                throw new ValidationException("Missing required parameter: id");
            }
            //passes id and than retrieves data
            $result = $this->interviewService->getInterview((int)$id);
            //returns result, and other errors will be thrown to error exception handler
            JsonResponse::success($result);
        }

        public function getInterviews(array $filters){
            //requires method get
            Request::requireMethod("GET");
            //extracts filters
            $categoryId = $filters['categoryId'] ?? null;
            $name = $filters['name'] ?? null;
            $difficulty = $filters['difficulty'] ?? null;
            //give filters to service
            $result = $this->interviewService->getInterviews($categoryId, $name, $difficulty);
            //returns result, and other errors will be thrown to error exception handler
            JsonResponse::success($result);
            
        }
    }
?>