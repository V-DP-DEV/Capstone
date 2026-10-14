<?php
    interface IInterviewRepository{
        public function getAllInterviewCategories():array;
        public function getUserInterview(int $id):array;
        public function getUserInterviewQuestions(int $id):array;
        public function getUserInterviews(?int $categoryId = null, ?string $name = null, ?string $difficulty = null);

        public function getInterviewCompetencies(int $interviewId): array;

        public function getQuestionCompetencies(int $interviewId): array;

        public function getQuestionConcepts(int $interviewId): array;
        public function getInterviewsQuestionTypes(array $interviewIds);
    }