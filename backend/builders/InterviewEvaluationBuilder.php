<?php
    class InterviewEvaluationBuilder{
        public function build($interview,$answers){
            return [
                'model'=>'gpt-4o-mini',
                'instructions'=>$this->buildInstructions(),
                'input'=>$this->buildInput($interview,$answers),
                'text'=>[
                    'format'=>[
                        'type'=>'json_schema',
                        'name'=>'question_evaluation',
                        'schema'=>$this->buildSchema(),
                        'strict'=>true
                    ]
                ]
            ];
        }

        private function buildInstructions(): string
        {
            return
                'Follow the instructions directly!' .
                '### ROLE ### ' .
                'You are an interviewer. Provide feedback on the following interview.' .
                '### FEEDBACK ### ' .
                'Only provide feedback based on the provided questions, expected answers, candidate answers, and criteria. ' .
                'Do not invent new criteria or requirements.';
        }

        private function buildInput($interview, $answers): string
        {
            return "hey";
            // Build entire interview input here
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
    }
?>