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
            return $this->interviewRepository->getUserInterview($id);
        }
        else if($this->requestContext->getRole() === 'ADMIN'){

        }
    }

    public function getInterviews(?int $categoryId = null, ?string $name = null, ?string $difficulty = null){
        //checks what role user is to retrieve the right data
        //no further checks needed
        if($this->requestContext->getRole() === 'USER'){
            return $this->interviewRepository->getUserInterviews($categoryId, $name, $difficulty);
        }
        else if($this->requestContext->getRole() === 'ADMIN'){

        }
    }
 }