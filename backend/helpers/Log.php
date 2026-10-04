<?php
    //log class
    class Log{
        public function __construct(private ILogRepository $logRepository){}
        //try to perform log, db failure should still allow action to complete
        public function tryAuthLog(int $userId,string $action,$tokenId = null){
            
            try{
                //try to do auth log db query
                return $this->logRepository->authLog($userId,$action,$tokenId);
            }
            //if any errors right to error log text file
            catch(Exception $e){
                error_log("Failed to log auth action: ".$e->getMessage());
            }
        }
    }
?>