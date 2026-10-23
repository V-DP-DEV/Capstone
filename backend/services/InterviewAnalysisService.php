<?php
    class InterviewAnalysisService{
        public function __construct(private Db $db,private RequestContext $request_context, private IInterviewAttemptRepository $interviewAttemptRepository, private InterviewEvaluationBuilder $evalBuilder,private IInterviewRepository $interviewRepository,private OpenAIService $aiService){

        }

        public function analysis(?int $attemptId){
            if(empty($attemptId)){
                throw new ValidationException("No attempt id was provided");
            }
            //get attempt
            $attempt  =$this->interviewAttemptRepository->getAttempt($attemptId);
            //do validation checks
            if(!$attempt){
                throw new ValidationException("No attempt provided");
            }
            if($attempt['analysed'] == 1){
                throw new ValidationException("Analysis has already been performed");
            }
            if($attempt['status']=== 'PROCESSING'){
                throw new ValidationException('Analysis is still being processed!');
            }
            if($attempt['user_id']!== $this->request_context->getUserId()){
                throw new UnauthorizedException("Not authorized to preform this action!");
            }
            $interviewId = $attempt['interview_id'];

            //update status to processing
            

            //get the questions, interviewCompetencies, questionCompetencies and questionConcepts to be analysed
            $questionsRows= $this->interviewAttemptRepository->getQuestionsForAnalysis($attemptId);
            $interviewCompetenciesRows = $this->interviewRepository->getInterviewCompetencies($interviewId);
            $questionCompetenciesRows = $this->interviewRepository->getQuestionCompetencies($interviewId);
            $questionConceptsRows = $this->interviewRepository->getQuestionConcepts($interviewId);
            
            //get the data from the AI by building the string and sending it
            $this->interviewAttemptRepository->updateAnalyseStatus($attemptId,'PROCESSING');
            $payload= $this->evalBuilder->build($questionsRows,$questionCompetenciesRows,$questionConceptsRows,$interviewCompetenciesRows);
            $data = $this->aiService->createResponse($payload);
            //extract low level data from response
            //return json_decode($data['usage'],true);
            $aiResponse=  json_decode($data['output'][0]['content'][0]['text'],true);
            //$aiTokenUsage = json_decode($data['usage']['input_tokens'],true);
            $inputTokens = json_decode($data['usage']['input_tokens'],true);
            $outputTokens = json_decode($data['usage']['output_tokens'],true);
            $aiQuestions = $aiResponse['questions'];
            $aiInterviewCompetencies = $aiResponse["interview_competencies"];
            
            //what will be saved to db
            $dbToSaveQuestionConcepts = [];
            $dbToSaveQuestionCompetencies = [];
            $dbToSaveInterviewCompetencies = [];

            //used to tally the scores of the AI
            $totalQuestionCompetencyScore = 0;
            $totalQuestionConceptScore = 0;
            $maxQuestionCompetencyScore = 0;
            $maxQuestionConceptScore = 0;

            //extract question and format for repository consumption
            foreach ($aiQuestions as $question) {

                $questionAttemptId = $question['question_attempt_id'];

                //extract competencies
                foreach ($question['competencies'] as $competency) {

                    $competencyId = $competency['competency_id'];
                    $score = $competency['score'];
                    $totalQuestionCompetencyScore+=$score;
                    $maxQuestionCompetencyScore+=5;

                    //map to what will be saved for db
                    $dbToSaveQuestionCompetencies[] = ['competency_breakdown_id'=>$competencyId,'score'=>$score,'question_attempt_id'=>$questionAttemptId];
                }
                
                //extract concepts
                foreach ($question['concepts'] as $concept) {

                    $conceptId = $concept['concept_id'];
                    $score = $concept['score'];
                    $totalQuestionConceptScore+=2;
                    $maxQuestionConceptScore+=2;

                    //map to what will be saved for db
                    $dbToSaveQuestionConcepts[] = ['concept_id'=>$conceptId,'score'=>$score,'question_attempt_id'=>$questionAttemptId];
                }
            }


            //extract interview competencies and format for repository consumption
            foreach($aiInterviewCompetencies as $interviewCompetency){
                $competencyId = $interviewCompetency['competency_id'];
                $strength = $interviewCompetency['strength'];
                $improvement= $interviewCompetency['improvement'];

                //map to what will be saved for db
                $dbToSaveInterviewCompetencies[]= [
                    'competency_breakdown_id'=>$competencyId,
                    'interview_attempt_id'=>$attemptId,
                    'strength'=>$strength,
                    'improvement'=>$improvement
                ];
            }
            
            //calculates percentage
            $percentage = ($totalQuestionCompetencyScore + $totalQuestionConceptScore) / ($maxQuestionCompetencyScore + $maxQuestionConceptScore);
            try{
                //begin a transaction adding all data needed and updating statuses
                $this->db->beginTransaction();
                $this->interviewAttemptRepository->addAnalyseInterview($dbToSaveInterviewCompetencies);
                $this->interviewAttemptRepository->addAnalyseQuestionCompetencies($dbToSaveQuestionCompetencies);
                $this->interviewAttemptRepository->addAnalyseQuestionConcepts($dbToSaveQuestionConcepts);
                $this->interviewAttemptRepository->updateScorePercentage($attemptId,$percentage);
                $this->interviewAttemptRepository->updateAttemptToAnalysed($attemptId);
                $this->interviewAttemptRepository->updateAnalyseStatus($attemptId,'COMPLETED');
                $this->interviewAttemptRepository->updateTokenUsage($attemptId,$inputTokens,$outputTokens);
                $this->db->commit();
            }
            //if errors
            catch(Exception $e){
                //rollback
                $this->db->rollBack();
                //update status to failed
                $this->interviewAttemptRepository->updateAnalyseStatus($attemptId,'FAILED');
                //re throw to global handler to handle
                throw $e;
            }
        }
    }