<?php
    class InterviewAnalysisService{
        public function __construct(private Db $db,private RequestContext $request_context, private IInterviewAttemptRepository $interviewAttemptRepository, private InterviewEvaluationBuilder $evalBuilder,private IInterviewRepository $interviewRepository,private OpenAIService $aiService){

        }

        public function analysis($attemptId){
            $attempt  =$this->interviewAttemptRepository->getAttempt($attemptId);
            if(!$attempt){
                throw new ValidationException("No attempt provided");
            }
            $interviewId = $attempt['interview_id'];
            $questionsRows= $this->interviewAttemptRepository->getQuestionsForAnalysis($attemptId);
            $interviewCompetenciesRows = $this->interviewRepository->getInterviewCompetencies($interviewId);
            $questionCompetenciesRows = $this->interviewRepository->getQuestionCompetencies($interviewId);
            $questionConceptsRows = $this->interviewRepository->getQuestionConcepts($interviewId);
            
            //get data
            $payload= $this->evalBuilder->build($questionsRows,$questionCompetenciesRows,$questionConceptsRows,$interviewCompetenciesRows);
            $data = $this->aiService->createResponse($payload);
            //extract low level data from response
            $aiResponse=  json_decode($data['output'][0]['content'][0]['text'],true);
            $aiQuestions = $aiResponse['questions'];
            $aiInterviewCompetencies = $aiResponse["interview_competencies"];
            
            $dbToSaveQuestionConcepts = [];
            $dbToSaveQuestionCompetencies = [];
            $dbToSaveInterviewCompetencies = [];

            //extract question and format for repository consumption
            foreach ($aiQuestions as $question) {

                $questionAttemptId = $question['question_attempt_id'];

                //extract competencies
                foreach ($question['competencies'] as $competency) {

                    $competencyId = $competency['competency_id'];
                    $score = $competency['score'];

                    $dbToSaveQuestionCompetencies[] = ['competency_breakdown_id'=>$competencyId,'score'=>$score,'question_attempt_id'=>$questionAttemptId];
                }
                
                //extract concepts
                foreach ($question['concepts'] as $concept) {

                    $conceptId = $concept['concept_id'];
                    $score = $concept['score'];

                    $dbToSaveQuestionConcepts[] = ['concept_id'=>$conceptId,'score'=>$score,'question_attempt_id'=>$questionAttemptId];
                }
            }


            //extract interview competencies and format for repository consumption
            foreach($aiInterviewCompetencies as $interviewCompetency){
                $competencyId = $interviewCompetency['competency_id'];
                $strength = $interviewCompetency['strength'];
                $improvement= $interviewCompetency['improvement'];

                $dbToSaveInterviewCompetencies[]= [
                    'competency_breakdown_id'=>$competencyId,
                    'interview_attempt_id'=>$attemptId,
                    'strength'=>$strength,
                    'improvement'=>$improvement
                ];
            }
            
            //rolls back
            $this->db->transaction(function() use ($dbToSaveInterviewCompetencies,$dbToSaveQuestionCompetencies,$dbToSaveQuestionConcepts){
                $this->interviewAttemptRepository->addAnalyseInterview($dbToSaveInterviewCompetencies);
                $this->interviewAttemptRepository->addAnalyseQuestionCompetencies($dbToSaveQuestionCompetencies);
                $this->interviewAttemptRepository->addAnalyseQuestionConcepts($dbToSaveQuestionConcepts);
            });
            
            return $aiResponse;
        }
    }