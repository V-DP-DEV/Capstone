<?php
    class InterviewAttemptService{
        public function __construct(private Db $db,private RequestContext $request_context, private IInterviewAttemptRepository $interviewAttemptRepository,private IInterviewRepository $interviewRepository){

        }

        public function submitAttempt($interviewId,$answers){
            //gets question ids and does validation checks
            $questionIds = array_column($answers, 'id');
            //check if duplicates
            if (count($questionIds) !== count(array_unique($questionIds))) {
                throw new ValidationException('Duplicate questions submitted.');
            }

            //check if questions belong to interview via db
            $validQuestions = $this->interviewAttemptRepository->getQuestionsMatching($questionIds,$interviewId);
            if (count($validQuestions) !== count($questionIds)) {
                throw new ValidationException('One or more questions do not belong to this interview.');
            }

            //submit the attempt to the db
            $userId = $this->request_context->getUserId();
            //rolls back if any errors
            return $this->db->transaction(function() use ($userId,$interviewId,$answers){
                //add the attempt
                $this->interviewAttemptRepository->addInterviewUserAttempt($userId,$interviewId);
                $attemptId = $this->db->lastInsertId();
                //add question attempt
                $this->interviewAttemptRepository->addInterviewUserAttemptQuestions($attemptId,$answers);
                return $attemptId;
            });
        }
        
        public function getAttempts(?string $sortBy =null,?string $sortOrder =null){
            //calls the repository 
            //check sort by and sort order
            $sortOption = new SortOptions($sortBy ?? 'completedAt',$sortOrder ?? 'DESC');

            //checks if attempts exists
            $userId = $this->request_context->getUserId();
            $attempts = $this->interviewAttemptRepository->getAttempts($userId,$sortOption);
            if(!$attempts){
                throw new ValidationException("No attempts currently");
            }

            //extracts interviewIds for further queries
            $interviewIds = array_column($attempts, 'interview_id');
            //gets the question types by using the extracted interviewIds
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
            //get attempt as base for to return
            $attempt = $this->interviewAttemptRepository->getAttemptDetails($id);
            //check if exists otherwise throw exception
            if(!$attempt){
                throw new ValidationException("No interview exists with that id");
            }
            //check if it is users attempt otherwise throw exception
            if($attempt['userId']!== $this->request_context->getUserId()){
                throw new ForbiddenException();
            }

            //get attempt questions
            $dbQuestions = $this->interviewAttemptRepository->getAttemptQuestions($id);
            
            //map the questions for each db row for frontend to consume
            $questions = [];
            foreach($dbQuestions as $row){
                $questionId = $row['id'];
                //if doesnt exist create new array element
                if(!isset($questions[$questionId])){
                    $questions[$questionId] = [
                        'id'=>$questionId,
                        'question'=>$row['question'],
                        'answer'=>$row['answer'],
                        'expectedAnswer'=>$row['expectedAnswer'],
                        'difficulty'=>$row['difficulty'],
                        'secondsSpent'=>$row['secondsSpent'],
                        'maxScore'=>$row['maxScore'],
                        'score'=>$row['score'],
                        'type'=>$row['type'],
                        'conceptScores'=>[]
                    ];
                }
                //add a concept for each row
                $questions[$questionId]['concepts'][] = [
                    'name'=>$row['concept_name'],
                    'maxScore'=>2,
                    'score'=>$row['concept_score'],
                    'id'=>$row['concept_id']
                ];
            }
            //than add the questions to the attempt object
            $attempt['questions'] = $questions;

            //check if analaysed
            if($attempt['analysed']){
                //than perform gettingg competencies interview feedback
                $dbCompetencies =$this->interviewAttemptRepository->getCompetenciesFeedback($id);
                $competencies = [];
                //map each competency according to their competency id
                foreach($dbCompetencies as $row){
                    $competencyId = $row['competency_id'];
                    
                    //ensures that we arent recreating the competencies
                    if(!isset($competencies[$competencyId])){
                        $competencies[$competencyId] =[
                            'name'=>$row['competency_name'],
                            'competencyPercentage'=>$row['competency_percentage'],
                            'strengths'=>[],
                            'improvements'=>[]
                        ];
                    }

                    //map each competency_breakdown according to
                    //the overall competency strength + improvement
                    //for frontend to consume
                    $competencies[$competencyId]['strengths'][] = [
                        'feedback'=>$row['strength'],
                        'breakdownPercentage'=>$row['breakdown_percentage'],
                        'name'=>$row['competency_breakdown_name']
                    ];
                    $competencies[$competencyId]['improvements'][] = [
                        'feedback'=>$row['improvement'],
                        'breakdownPercentage'=>$row['breakdown_percentage'],
                        'name'=>$row['competency_breakdown_name']
                    ];
                }
                //when all competencies are mapped add to attempt
                $attempt['competencies'] = $competencies;
            }   

            $attempt['questions'] = $questions;
            return $attempt;
        }

        public function deleteAttempt($id){
            //gets attempt
            $attempt = $this->interviewAttemptRepository->getAttempt($id);
            //check if exists and belongs to user
            if(!$attempt){
                throw new ValidationException("No interview exists with that id");
            }
            if($attempt['user_id']!== $this->request_context->getUserId()){
                throw new ForbiddenException();
            }
            //performs delete
            return $this->interviewAttemptRepository->deleteAttempt($id);
            //call the delete function
        }

        public function deleteAttempts(){
            //gets user id and deletes all attempts
            $userId= $this->request_context->getUserId();
            return $this->interviewAttemptRepository->deleteAttempts($userId);
            //call the delete all function
        }

        public function setToReviewed(int $attemptId){
            //get attempt and do ownership checks
            $attempt= $this->interviewAttemptRepository->getAttempt($attemptId);
            if(!$attempt){
                throw new ValidationException("No interview exists with that id");
            }
            if($attempt['user_id']!== $this->request_context->getUserId()){
                throw new ForbiddenException();
            }
            //due set To Reviewed
            return $this->interviewAttemptRepository->setToReviewed($attemptId);
        }
    }
?>