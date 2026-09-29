<?php
    //log class
    class Log{
        //try to perform log, db failure should still allow action to complete
        public static function tryAuthLog(int $userId,string $action,$tokenId = null){
            
            try{
                //try to do auth log db query
                return LogRepository::authLog($userId,$action,$tokenId);
            }
            //if any errors right to error log text file
            catch(Exception $e){
                error_log("Failed to log auth action: ".$e->getMessage());
            }
        }
    }
?>