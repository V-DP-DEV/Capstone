<?php
    class InterviewEvaluationBuilder{
        public function build($interviewQuestionResponse, $questionCompetencies,$questionConcepts,$interviewCompetency){

            return [
                'model'=>'gpt-4o-mini',
                'instructions'=>$this->buildInstructions(),
                'input'=>$this->buildInput($interviewQuestionResponse, $questionCompetencies,$questionConcepts,$interviewCompetency),
                'text'=>$this->buildOutput()
            ];
            //return ['input'=>$this->buildInput($interviewQuestionResponse, $questionCompetencies,$questionConcepts,$interviewCompetency)];

        }

        private function buildInstructions(): string
        {
            return
                'Follow the instructions directly! ' .
'### ROLE ### ' .
'You are an interviewer evaluating a candidate interview. ' .

'### EVALUATION SCOPE ### ' .
'-Only evaluate information contained in the provided question, expected answer, candidate answer, and the competencies and concepts explicitly associated with that question. ' .
'-Do not invent, infer, or assume additional competencies, concepts, requirements, experience, or knowledge that is not demonstrated by the candidate. ' .

'### SCORING PRINCIPLES ### ' .
'-Score every competency and concept independently based only on demonstrated evidence. ' .
'-Do not score based on answer length, confidence, writing style, or perceived potential. ' .
'-Do not let the score of one competency or concept influence the score of another. ' .
'-A concise answer can receive a high score when it directly and correctly demonstrates the required evidence. ' .
'-Do not assume that demonstrating one concept means that another concept was demonstrated. ' .
'-Do not assume that demonstrating one competency means that another competency was demonstrated. ' .
'-Score only evidence that is actually present in the candidate answer. ' .
'-Absence of evidence should not be interpreted as partial evidence. ' .
'-Partial evidence should receive credit only for what is demonstrated.. ' .
'-When the expected answer contains multiple pieces of information, do not require all pieces unless they are relevant to the specific competency or concept being scored. ' .
'-Incorrect or contradictory information should reduce the score when it directly relates to the competency or concept being evaluated. ' .
'-When the evidence falls between two scores, assign the lower score. ' .

'### SCORING RUBRIC ### ' .
'-Use the following rubric consistently for every competency and concept. ' .

'-0 = Not demonstrated. ' .
'No relevant evidence is provided. ' .

'-1 = Minimally demonstrated. ' .
'There is only weak, vague, incomplete, or indirect evidence of the competency or concept. ' .

'-2 = Partially demonstrated. ' .
'The candidate demonstrates some relevant and correct evidence, but the core requirement is not fully demonstrated.' .

'-3 = Adequately demonstrated. ' .
'The candidate demonstrates the core requirement with a generally correct and relevant response but with limited depth,precision,completeness,or supporting detail' .

'-4 = Strongly demonstrated. ' .
'The candidate clearly and correctly demonstrates the core requirement with specific and relevant evidence with minor gaps' .

'-5 = Fully demonstrated. ' .
'The candidate demonstrates the competency or concept comprehensively, accurately, and convincingly.' .


'### STRENGTHS + IMPROVEMENTS ### ' .
'-Generate interview_competencies as a summary of the competency scores already assigned in the questions. ' .
'-Provide exactly one concise sentence describing the strongest demonstrated aspect of the competency. ' .
'-Provide exactly one concise sentence describing the most important improvement for that competency. ' .
'-The strength must be supported by evidence from the interview. ' .
'-The improvement must identify something that could realistically be improved based on the candidates demonstrated performance. ' .
'-Do not create a weakness merely because the candidate did not demonstrate an unrelated requirement. ' .
'-If the competency has no meaningful demonstrated strength across the interview, use exactly: "No clear strength demonstrated." ' .
'-Strengths and improvements must relate only to the specific competency and must not discuss unrelated competencies or concepts. ' .
'-Do not invent evidence, requirements, competencies, or concepts. ';
        }

        private function buildInput($interviewQuestionResponse, $questionCompetencies,$questionConcepts,$interviewCompetency):string
        {
            $questions = $this->buildInputQuestionResponse($interviewQuestionResponse,$questionCompetencies,$questionConcepts);
            $interviewCompetencies = $this->buildInputInterviewCompetency($interviewCompetency);
            //return ['interview_competencies'=>$interviewCompetencies];
            return json_encode(['questions'=>$questions,'interview_competencies'=>$interviewCompetencies]);
        }

        private function buildInputQuestionResponse($interviewQuestionResponses,$questionCompetencies,$questionConcepts){
            //get competencies + concepts
            $competenciesByQuestion = $this->buildInputQuestionCompetency($questionCompetencies);
            $conceptsByQuestion = $this->buildInputQuestionConcept($questionConcepts);
        
            $questions = [];
            foreach($interviewQuestionResponses as $row){
                $questionId = $row['id'];
                $questions[] = [
                    'id'=>$questionId,
                    'question'=>$row['question'],
                    'answer'=>$row['answer'],
                    'expected_answer'=>$row['expected_answer'],
                    'comptencies'=> $competenciesByQuestion[$questionId]?? [],
                    'concepts'=> $conceptsByQuestion[$questionId] ?? []
                ];
            }
            return $questions;
        }

        private function buildInputQuestionCompetency($questionCompetencies){
            $competenciesByQuestion = [];

            foreach ($questionCompetencies as $row) {
                $competenciesByQuestion[$row['question_id']][] = [
                    'id' => $row['competency_breakdown_id'],
                    'name' => $row['competency_name'],
                ];
            }
            return $competenciesByQuestion;
        }

        private function buildInputQuestionConcept($questionConcepts){
            $conceptsByQuestion =[];
            foreach ($questionConcepts as $row) {
                $conceptsByQuestion[$row['question_id']][] = [
                    'id' => $row['concept_id'],
                    'name' => $row['name'],
                ];
            }
            return $conceptsByQuestion;
        }

        private function buildInputInterviewCompetency($interviewCompetencies){
            return $interviewCompetencies;
        }

        private function buildSchema(): array
        {
            return [
                'type' => 'object',
                'properties' => [
                    'feedback' => [
                        'type' => 'string'
                    ]
                ],
                'required' => ['feedback'],
                'additionalProperties' => false
                ];
            // JSON schema here
        }

        private function buildOutput(){
            return ["format" => [
    "type" => "json_schema",
    "name" => "question_evaluation",
    "schema" => [
        "type" => "object",
        "properties" => [
            "questions" => [
                "type" => "array",
                "items" => [
                    "type" => "object",
                    "properties" => [
                        "question_id" => [
                            "type" => "integer"
                        ],
                        "competencies" => [
                            "type" => "array",
                            "items" => [
                                "type" => "object",
                                "properties" => [
                                    "competency_id" => [
                                        "type" => "integer"
                                    ],
                                    "score" => [
                                        "type" => "integer",
                                        "minimum" => 0,
                                        "maximum" => 5
                                    ]
                                ],
                                "required" => [
                                    "competency_id",
                                    "score"
                                ],
                                "additionalProperties" => false
                            ]
                        ],
                        "concepts" => [
                            "type" => "array",
                            "items" => [
                                "type" => "object",
                                "properties" => [
                                    "concept_id" => [
                                        "type" => "integer"
                                    ],
                                    "score" => [
                                        "type" => "integer",
                                        "minimum" => 0,
                                        "maximum" => 5
                                    ]
                                ],
                                "required" => [
                                    "concept_id",
                                    "score"
                                ],
                                "additionalProperties" => false
                            ]
                        ]
                    ],
                    "required" => [
                        "question_id",
                        "competencies",
                        "concepts"
                    ],
                    "additionalProperties" => false
                ]
            ],
            "interview_competencies" => [
                "type" => "array",
                    "items" => [
                        "type" => "object",
                        "properties" => [
                            "competency_id" => [
                                "type" => "integer"
                            ],
                            "strength" => [
                                "type" => "string"
                            ],
                            "improvement" => [
                                "type" => "string"
                            ]
                        ],
                        "required" => [
                            "competency_id",
                            "strength",
                            "improvement"
                        ],
                        "additionalProperties" => false
                    ]
                ]
        ],
        "required" => [
            "questions",
            "interview_competencies"
        ],
        "additionalProperties" => false
    ],
    "strict" => true
]];
        }
    }
?>