<?php 
    class AnalyticsController{
        public function __construct(private AnalyticsService $analyticsService){} 

        public function concepts(array $filters){
            Request::requireMethod("GET");
            //extract filters
            $sortBy = $filters['sortBy'] ?? null;
            $sortOrder = $filters['sortOrder'] ?? null;
            $last = $filters['lastInterviews'] ?? null;
            //try and do it unless error is thrown
            $response = $this->analyticsService->getConceptsAnalytics($sortBy,$sortOrder,$last);
            JsonResponse::success($response);
        }

        public function competencies(array $filters){
             Request::requireMethod("GET");
            //extract filters
            $sortBy = $filters['sortBy'] ?? null;
            $sortOrder = $filters['sortOrder'] ?? null;
            $last = $filters['lastInterviews'] ?? null;
            //try and do it unless error is thrown
            $response = $this->analyticsService->getCompetenciesAnalytics($sortBy,$sortOrder,$last);
            JsonResponse::success($response);
        }
    }