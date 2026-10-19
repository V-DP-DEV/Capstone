<?php class AnalyticsService{
    public function __construct(
            private IAnalyticsRepository $interviewAnalyticsRepository,
            private Log $log,
            private Db $db,
            private RequestContext $request_context
        ){}

    public function getCompetenciesAnalytics(string $sortBy,string $sortOrder,int $last){
        $sortOption = new SortOptions($sortBy ?? 'percentage',$sortOrder ?? 'DESC');

        $dbCompetencies = $this->interviewAnalyticsRepository->getCompetenciesAnalytics($sortOption,$last,$this->request_context->getUserId());
        if(!$dbCompetencies){
            throw new ValidationException("No attempts analysed");
        }
        $competencies = [];
            foreach($dbCompetencies as $row){
                $competencyId = $row['competency_id'];
                //if doesnt exist create new array element
                if(!isset($competencies[$competencyId])){
                    $competencies[$competencyId] = [
                        'id'=>$competencyId,
                        'name'=>$row['competency_name'],
                        'percentage'=>$row['competency_percentage'],
                    ];
                }
                //add a concept for each row
                $competencies[$competencyId]['competenciesBreakdownAnalysisList'][] = [
                    'name'=>$row['competency_breakdown_name'],
                    'percentage'=>$row['competency_breakdown_percentage'],
                ];
            }

        $result = array_values($competencies);

        return $result;
    }

    public function getConceptsAnalytics(string $sortBy,string $sortOrder,int $last){
        $sortOption = new SortOptions($sortBy ?? 'percentage',$sortOrder ?? 'DESC');
        $dbResult = $this->interviewAnalyticsRepository->getConceptsAnalytics($sortOption,$last,$this->request_context->getUserId());
        if(!$dbResult){
            throw new ValidationException("No attempts analysed");
        }
        return $dbResult;
    }
}