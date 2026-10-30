<?php
    interface IInterviewRepository{
        public function getAllInterviewCategories():array|false;
        public function getUserInterview(int $id):array|false;
        public function getUserInterviewQuestions(int $id):array|false;
        public function getUserInterviews(int $userId,?int $categoryId = null, ?string $name = null, ?string $difficulty = null):array|false;

        public function getInterviewCompetencies(int $interviewId): array|false;

        public function getQuestionCompetencies(int $interviewId): array|false;

        public function getQuestionConcepts(int $interviewId): array|false;
        public function getInterviewsQuestionTypes(array $interviewIds):array|false;
    }