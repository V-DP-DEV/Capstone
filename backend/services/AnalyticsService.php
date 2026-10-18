<?php class AnalyticsService{
    public function __construct(
            private IAnalyticsRepository $interviewAnalyticsRepository,
            private Log $log,
            private Db $db,
            private RequestContext $request_context
        ){}

    public function getCompetenciesAnalytics(string $sortBy,string $sortOrder,int $last){
        $sortOption = new SortOptions($sortBy ?? 'score',$sortOrder ?? 'DESC');
        return $this->interviewAnalyticsRepository->getCompetenciesAnalytics($sortOption,$last,$this->request_context->getUserId());
    }

    public function getConceptsAnalytics(string $sortBy,string $sortOrder,int $last){
        $sortOption = new SortOptions($sortBy ?? 'score',$sortOrder ?? 'DESC');
        return $this->interviewAnalyticsRepository->getConceptsAnalytics($sortOption,$last,$this->request_context->getUserId());
    }
}