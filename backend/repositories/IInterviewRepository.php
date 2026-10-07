<?php
    interface IInterviewRepository{
        public function getAllInterviewCategories();
        public function getUserInterview(int $id);
        public function getUserInterviews(?int $categoryId = null, ?string $name = null, ?string $difficulty = null);

        public function getInterviewCompetencies(int $interviewId): array;

        public function getQuestionCompetencies(int $interviewId): array;

        public function getQuestionConcepts(int $interviewId): array;
    }