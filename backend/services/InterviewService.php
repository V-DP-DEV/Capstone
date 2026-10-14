<?php
 class InterviewService{
     public function __construct(
            private Db $db,
            private InterviewRepository $interviewRepository,
            private RequestContext $requestContext
        ){}

    public function getAllInterviewCategories(){
        //returns the result. No checks needed
        return $this->interviewRepository->getAllInterviewCategories();
    }

    public function getInterview(int $id){
        //checks what role user is to retrieve the right data
        //no further checks needed
        if($this->requestContext->getRole() === 'USER'){
            $interview = $this->interviewRepository->getUserInterview($id);
            $questions = $this->interviewRepository->getUserInterviewQuestions($id);
            $interview['questions']=$questions;
            return $interview;
        }
        else if($this->requestContext->getRole() === 'ADMIN'){

        }
    }

    public function getInterviews(?int $categoryId = null, ?string $name = null, ?string $difficulty = null){
        //checks what role user is to retrieve the right data
        //no further checks needed
        if($this->requestContext->getRole() === 'USER'){
            //get interviews
            $interviews = $this->interviewRepository->getUserInterviews($categoryId,$name,$difficulty);
            //extract interview ids
            $interviewIds = array_column($interviews, 'id');
            //get interview question types
            $interviewQuestionsTypes = $this->interviewRepository->getInterviewsQuestionTypes($interviewIds);
            //group them in object
            $typesByInterview = [];
            foreach ($interviewQuestionsTypes as $question) {
                $typesByInterview[$question['interview_id']][] = $question['name'];
            }

            //add question type to interview
            foreach ($interviews as &$interview) {
                $id = $interview['id'];

                $interview['questionTypes'] =$typesByInterview[$id] ?? [];
                unset($interview);
            }
            return $interviews;
        }
        else if($this->requestContext->getRole() === 'ADMIN'){

        }
    }
 }