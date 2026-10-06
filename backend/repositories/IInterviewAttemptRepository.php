<?php
    interface IInterviewAttemptRepository{
        public function submitAttempt($userId,$interviewId,$answers);
    }