<?php
 class InterviewService{
     public function __construct(
            private Db $db,
            private InterviewRepository $interviewRepository,
            private RequestContext $requestContext
        ){}

    public function getAllInterviewCategories(){
        return $this->interviewRepository->getAllInterviewCategories();
    }

    public function getInterview(int $id){
        if($this->requestContext->getRole() === 'USER'){
            return $this->interviewRepository->getUserInterview($id);
        }
        else if($this->requestContext->getRole() === 'ADMIN'){

        }
    }

    public function getInterviews(?int $categoryId = null, ?string $name = null, ?string $difficulty = null){

        if($this->requestContext->getRole() === 'USER'){
            return $this->interviewRepository->getUserInterviews($categoryId, $name, $difficulty);
        }
        else if($this->requestContext->getRole() === 'ADMIN'){

        }
    }
 }