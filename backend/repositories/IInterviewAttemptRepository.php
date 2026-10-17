<?php
    interface IInterviewAttemptRepository{
        public function addInterviewUserAttempt(int $userId,int $interviewId):int;
        public function addInterviewUserAttemptQuestions(int $attemptId, array $answers):int;

        public function getQuestionsForAnalysis(int $attemptId): array;
        public function getAttempt(int $attemptId):array;
        public function getAttemptDetails(int $attemptId):array;
        public function getAttemptQuestions(int $attemptId):array;
         public function getAttempts(int $userId, SortOptions $sortOptions);
         public function getCompetenciesFeedback($attemptId);
        public function addAnalyseQuestionConcepts($aiQuestionsConcepts);
        public function addAnalyseInterview($aiInterviewCompetencies);
        public function addAnalyseQuestionCompetencies($aiQuestionCompetencies);
        public function updateAttemptToAnalysed($interviewAttemptId);
        public function updateAnalyseStatus($interviewAttemptId,$status);
        public function updateScorePercentage($interviewAttemptId,$percentage);
        public function getQuestionsMatching($questionIds,$interviewId);
        public function deleteAttempt($interviewAttemptId);
        public function deleteAttempts($userId);
        public function setToReviewed(int $attemptId):int;
        
    }