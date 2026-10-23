<?php
    interface IAnalyticsRepository{
        public function getCompetenciesAnalytics(SortOptions $sortOptions,int $last,int $userId):array|false;
        public function getConceptsAnalytics(SortOptions $sortOptions,int $last,int $userId):array|false;
        public function getUserInterviewMetrics(int $userId):array|false;
    }