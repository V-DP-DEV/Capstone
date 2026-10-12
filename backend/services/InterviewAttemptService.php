<?php
    class InterviewAttemptService{
        public function __construct(private Db $db,private RequestContext $request_context, private IInterviewAttemptRepository $interviewAttemptRepository){

        }

        public function submitAttempt($interviewId,$answers){
            //TO:DO
            //do additional checks to check if attempt already made?
            //is being done by constraints
            $questionIds = array_column($answers, 'id');
            if (count($questionIds) !== count(array_unique($questionIds))) {
                throw new ValidationException('Duplicate questions submitted.');
            }

            $validQuestions = $this->interviewAttemptRepository->getQuestionsMatching($questionIds,$interviewId);
            if (count($validQuestions) !== count($questionIds)) {
                throw new ValidationException('One or more questions do not belong to this interview.');
            }

            $userId = $this->request_context->getUserId();
            return $this->db->transaction(function() use ($userId,$interviewId,$answers){
                return $this->interviewAttemptRepository->submitAttempt($userId,$interviewId,$answers);
            });
        }
        
        public function getAttempts($sortBy,$sortOrder){
            //calls the repository 
        }

        public function getAttempt($id){
            $attempt = $this->interviewAttemptRepository->getAttempt($id);
            if(!$attempt){
                throw new ValidationException("No interview exists with that id");
            }
            if($attempt['user_id']!== $this->request_context->getUserId()){
                throw new ForbiddenException();
            }
            //get / construct attempt to return
        }

        public function deleteAttempt($id){
            $attempt = $this->interviewAttemptRepository->getAttempt($id);
            if(!$attempt){
                throw new ValidationException("No interview exists with that id");
            }
            if($attempt['user_id']!== $this->request_context->getUserId()){
                throw new ForbiddenException();
            }
            //call the delete function
        }

        public function deleteAttempts(){
            $userId= $this->request_context->getUserId();
            //call the delete all function
        }
    }
?>