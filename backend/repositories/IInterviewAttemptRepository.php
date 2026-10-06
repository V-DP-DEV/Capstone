<?php
    interface IInterviewAttemptRepository{
        public function submitAttempt($interviewId,$answers);
    }