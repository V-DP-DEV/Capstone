<?php
    class InterviewAttemptService{
        public function __construct(private Db $db,private RequestContext $request_context, private IInterviewAttemptRepository $interviewAttemptRepository,private IInterviewRepository $interviewRepository){

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
        
        public function getAttempts(?string $sortBy =null,?string $sortOrder =null){
            //calls the repository 
            //check sort by and sort order
            $sortOption = new SortOptions($sortBy ?? 'completedAt',$sortOrder ?? 'DESC');

            $userId = $this->request_context->getUserId();
            $attempts = $this->interviewAttemptRepository->getAttempts($userId,$sortOption);
            if(!$attempts){
                throw new ValidationException("No attempts currently");
            }

            $interviewIds = array_column($attempts, 'interview_id');
            $questionTypes = $this->interviewRepository->getInterviewsQuestionTypes($interviewIds);

            //group them in object
            $typesByInterview = [];
            foreach ($questionTypes as $question) {
                $typesByInterview[$question['interview_id']][] = $question['name'];
            }

            //add question type to interview
            foreach ($attempts as &$attempt) {
                $id = $attempt['interview_id'];

                $attempt['questionTypes'] =$typesByInterview[$id] ?? [];
                unset($attempt);
            }
            return $attempts;
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
            return $this->deleteAttempt($id);
            //call the delete function
        }

        public function deleteAttempts(){
            $userId= $this->request_context->getUserId();
            return $this->deleteAttempts();
            //call the delete all function
        }
    }
?>