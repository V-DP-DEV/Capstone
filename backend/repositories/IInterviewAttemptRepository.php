<?php
    interface IInterviewAttemptRepository{
        public function submitAttempt($userId,$interviewId,$answers);
        public function getQuestionsForAnalysis(int $attemptId): array;
        public function getAttempt(int $attemptId):array;
        public function addAnalyseQuestionConcepts($aiQuestionsConcepts);
        public function addAnalyseInterview($aiInterviewCompetencies);
        public function addAnalyseQuestionCompetencies($aiQuestionCompetencies);
        public function updateAttemptToAnalysed($interviewAttemptId);
    }