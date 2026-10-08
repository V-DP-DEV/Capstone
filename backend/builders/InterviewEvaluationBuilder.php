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

'### SCORING RUBRIC ### ' .
'-Use the following rubric consistently for every competency and concept. ' .

'-0 = Not demonstrated. ' .
'No relevant evidence.' .

'-1 = Minimally demonstrated. ' .
'Weak, vague, or very limited evidence' .

'-2 = Partially demonstrated. ' .
'Some relevant evidence, but important gaps or errors remain.' .

'-3 = Adequately demonstrated. ' .
'Sufficient and generally correct evidence, but limited depth or completeness.' .

'-4 = Strongly demonstrated. ' .
'Clear and correct evidence with only minor gaps' .

'-5 = Fully demonstrated. ' .
' Comprehensive and accurate evidence covering the important aspects relevant to the question..' .
"Before assigning a score, identify the evidence in the candidate's answer that is relevant to the specific competency or concept. Then assign the score based only on that evidence.".


'### STRENGTHS + IMPROVEMENTS ### ' .
'-The interview_competencies section is a reporting layer, not an evaluation layer. ' .
'-Its purpose is to describe the results of the question-level competency evaluation, not to perform a second evaluation. ' .
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
                $questionAttemptId = $row['question_attempt_id'];
                $questions[] = [
                    'question_attempt_id'=>$questionAttemptId,
                    'question'=>$row['question'],
                    'answer'=>$row['answer'],
                    'expected_answer'=>$row['expected_answer'],
                    'competencies'=> $competenciesByQuestion[$questionId]?? [],
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
                        "question_attempt_id" => [
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
                        "question_attempt_id",
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