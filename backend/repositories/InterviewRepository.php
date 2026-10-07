<?php
    class InterviewRepository implements IInterviewRepository{
        public function __construct(private Db $db){}
        
        public function getAllInterviewCategories(){
            $query = "SELECT * FROM interview_categories";
            return $this->db->queryAll($query);
        }

        public function getUserInterview(int $id){
            //map as to match frontend
            $query = "SELECT id,interview_name as name,difficulty,estimated_time_in_minutes as duration FROM interviews WHERE id = ?";
            $interview = $this->db->queryOne($query,[$id]);
            
            $query = "SELECT iq.id,iq.difficulty,iq.text,qt.name FROM interview_questions iq JOIN question_types qt ON iq.type_id = qt.id WHERE iq.active =1 AND iq.interview_id=?";
            $interviewQuestions = $this->db->queryAll($query,[$id]);
            $interview['questions'] = $interviewQuestions;
            return $interview;
        }

        public function getUserInterviews(?int $categoryId = null, ?string $name = null, ?string $difficulty = null){
            //map name for frontEnd purposes
            $query = "SELECT id,interview_name as name,difficulty FROM interviews WHERE 1=1";

            //add filters
            $params = [];
            if($categoryId !== null){
                $query .= " AND category_id = ?";
                $params[] = $categoryId;
            }
            
            if($name !== null){
                $query .= " AND interview_name LIKE ?";
                $params[] = "%$name%";
            }
            if($difficulty !== null){
                $query .= " AND difficulty = ?";
                $params[] = $difficulty;
            }
            $generalInterview = $this->db->queryAll($query,$params);

            //if no results return nothing
            if(!$generalInterview){
                return [];
            }

            //get interview ids
            $interviewIds = array_column($generalInterview, 'id');
            //create place holder for sql
            $placeHolders = implode(',', array_fill(0, count($interviewIds), '?'));

            //retrieve the question types related
            $query = "SELECT qt.name,iq.interview_id FROM interview_questions iq JOIN question_types qt ON iq.type_id = qt.id WHERE iq.interview_id IN ($placeHolders)";

            $interviewQuestions = $this->db->queryAll($query, $interviewIds);


            //group them in object
            $typesByInterview = [];
            foreach ($interviewQuestions as $question) {
                $typesByInterview[$question['interview_id']][] = $question['name'];
            }

            //add question type to interview
            foreach ($generalInterview as &$interview) {
                $id = $interview['id'];

                $interview['questionTypes'] =$typesByInterview[$id] ?? [];
                unset($interview);
            }
            return $generalInterview;
        }

        public function getInterviewCompetencies(int $interviewId): array{
            $query = "SELECT DISTINCT cb.id,cb.name
                        FROM interview_questions iq 
                        JOIN question_types qt ON iq.type_id=qt.id
                        JOIN question_type_competency_breakdowns qtc ON qtc.question_type_id=qt.id
                        JOIN competency_breakdowns cb ON cb.competency_id=qtc.competency_breakdown_id
                        WHERE iq.interview_id=?
                        ORDER BY id ASC";
            return $this->db->queryAll($query,[$interviewId]);
        }

        public function getQuestionCompetencies(int $interviewId): array{
            $query = "SELECT iq.id as question_id,cb.id as competency_breakdown_id,cb.name as competency_name FROM interview_questions iq 
	                    JOIN question_types qt ON iq.type_id=qt.id
                        JOIN question_type_competency_breakdowns qtc ON qtc.question_type_id=qt.id
                        JOIN competency_breakdowns cb ON qtc.competency_breakdown_id=cb.id
                        WHERE iq.interview_id=?
                        ORDER BY question_id ASC";
            return $this->db->queryAll($query,[$interviewId]);
        }

        public function getQuestionConcepts(int $interviewId): array{
            $query = "SELECT iq.id as question_id,c.id as concept_id, c.name
                        FROM interview_questions iq 
                        JOIN interview_question_concepts iqc ON iqc.question_id = iq.id
                        JOIN concepts c ON c.id= iqc.concept_id
                        WHERE iq.interview_id=?
                        ORDER BY question_id ASC";
            return $this->db->queryAll($query,[$interviewId]);
        }
    }