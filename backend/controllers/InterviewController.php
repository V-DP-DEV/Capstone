<?php
    class InterviewController{
        public function __construct(private InterviewService $interviewService){}

        public function getAllInterviewCategories(){
            Request::requireMethod("GET");
            $result = $this->interviewService->getAllInterviewCategories();
            JsonResponse::success($result);
        }

        public function getInterview(array $filters){
            Request::requireMethod("GET");
            $id = $filters['id'] ?? null;
            if($id === null){
                throw new ValidationException("Missing required parameter: id");
            }
            $result = $this->interviewService->getInterview((int)$id);
            JsonResponse::success($result);
        }

        public function getInterviews(array $filters){
            Request::requireMethod("GET");
            $categoryId = $filters['categoryId'] ?? null;
            $name = $filters['name'] ?? null;
 
            
            $difficulty = $filters['difficulty'] ?? null;
            $result = $this->interviewService->getInterviews($categoryId, $name, $difficulty);
            JsonResponse::success($result);
            
        }
    }
?>