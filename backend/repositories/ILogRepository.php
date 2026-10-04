<?php
    interface ILogRepository{
        //log method for auth
        //log ip
        public function authLog(int $userId,string $action,$tokenId = null);
    }
?>