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
            
        
            $payload= $this->evalBuilder->build($questionsRows,$questionCompetenciesRows,$questionConceptsRows,$interviewCompetenciesRows);
            return $this->aiService->createResponse($payload);
            //return ["questions"=>$questionsRows]
        }
    }