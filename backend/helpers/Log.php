<?php
    class Log{
        public static function tryAuthLog(int $userId,string $action,$tokenId = null){
            try{
                return LogRepository::authLog($userId,$action,$tokenId);
            }
            catch(Exception $e){
                error_log("Failed to log auth action: ".$e->getMessage());
            }
        }
    }
?>