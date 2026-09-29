<?php
    class LogRepository{
        //log method for auth
        //log ip
        public static function authLog(int $userId,string $action,$tokenId = null){
            return Db::execute("INSERT INTO auth_logs (user_id,action,token_id,ip_address) VALUES (?,?,?,?)",[$userId,$action,$tokenId,$_SERVER['REMOTE_ADDR']]);
        }
    }
?>