<?php
    interface IInterviewAttemptRepository{
        public function addInterviewUserAttempt(int $userId,int $interviewId):int;
        public function addInterviewUserAttemptQuestions(int $attemptId, array $answers):int;

        public function getQuestionsForAnalysis(int $attemptId): array|false;
        public function getAttempt(int $attemptId):array|false;
        public function getAttemptDetails(int $attemptId):array|false;
        public function getAttemptQuestions(int $attemptId):array|false;
         public function getAttempts(int $userId, SortOptions $sortOptions):array|false;
         public function getCompetenciesFeedback(int $attemptId):array|false;
        public function addAnalyseQuestionConcepts(mixed $aiQuestionsConcepts):int;
        public function addAnalyseInterview(mixed $aiInterviewCompetencies):int;
        public function addAnalyseQuestionCompetencies(mixed $aiQuestionCompetencies):int;
        public function updateAttemptToAnalysed(int $interviewAttemptId):int;
        public function updateAnalyseStatus(int $interviewAttemptId,string $status):int;
        public function updateScorePercentage(int $interviewAttemptId,float $percentage):int;
        public function getQuestionsMatching(array $questionIds,int $interviewId):array|false;
        public function deleteAttempt(int $interviewAttemptId);
        public function deleteAttempts(int $userId);
        public function setToReviewed(int $attemptId):int;
        public function updateTokenUsage(int $attemptId, int $inputTokens, int $outputTokens):int;
    }