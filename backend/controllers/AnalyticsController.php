<?php 
    class AnalyticsController{
        public function __construct(private AnalyticsService $analyticsService){} 

        public function concepts(array $filters){
            $sortBy = $filters['sortBy'] ?? null;
            $sortOrder = $filters['sortOrder'] ?? null;
            $last = $filters['lastInterviews'] ?? null;
            $response = $this->analyticsService->getConceptsAnalytics($sortBy,$sortOrder,$last);
            JsonResponse::success([$response]);
        }

        public function competencies(array $filters){
            $sortBy = $filters['sortBy'] ?? null;
            $sortOrder = $filters['sortOrder'] ?? null;
            $last = $filters['lastInterviews'] ?? null;
            $response = $this->analyticsService->getCompetenciesAnalytics($sortBy,$sortOrder,$last);
            JsonResponse::success([$response]);
            //JsonResponse::success([$response]);
        }
    }