<?php
    interface ILogRepository{
        //log method for auth
        //log ip
        public function authLog(int $userId,string $action,$tokenId = null):int;
        public function auditLog(int $userId,string $action,string $entityType,int $entityId):int;
    }
?>