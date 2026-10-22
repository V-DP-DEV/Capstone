<?php class AnalyticsService{
    public function __construct(
            private IAnalyticsRepository $interviewAnalyticsRepository,
            private Log $log,
            private Db $db,
            private RequestContext $request_context
        ){}

    public function getCompetenciesAnalytics(string $sortBy,string $sortOrder,int $last){
        //create sort object
        $sortOption = new SortOptions($sortBy ?? 'percentage',$sortOrder ?? 'DESC');

        //get competencies from db
        $dbCompetencies = $this->interviewAnalyticsRepository->getCompetenciesAnalytics($sortOption,$last,$this->request_context->getUserId());
        //if none throw error
        if(!$dbCompetencies){
            throw new ValidationException("No attempts analysed");
        }
        //extract and map competencies
        $competencies = [];
            //go through each row
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

        //ensures its an array and not map as created earlier
        $result = array_values($competencies);

        return $result;
    }

    public function getConceptsAnalytics(string $sortBy,string $sortOrder,int $last){
        //create sort object
        $sortOption = new SortOptions($sortBy ?? 'percentage',$sortOrder ?? 'DESC');
        //tries to get the result
        $dbResult = $this->interviewAnalyticsRepository->getConceptsAnalytics($sortOption,$last,$this->request_context->getUserId());
        //if none throw error
        if(!$dbResult){
            throw new ValidationException("No attempts analysed");
        }
        //if result return
        return $dbResult;
    }

    public function userDashboard(){
        $result = $this->interviewAnalyticsRepository->getUserInterviewMetrics($this->request_context->getUserId());
        return $result;
    }
}