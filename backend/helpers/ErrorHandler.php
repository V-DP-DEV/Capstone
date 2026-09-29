<?php
    //global errorhandler to handle errors
    class ErrorHandler{
        public static function handle(Throwable $e){
            //if db error
            if($e instanceof PDOException){
                //create new error object and replace
                $e = DbConstraintMapper::map($e);
            }

            //if type ApiException
            if($e instanceof ApiException){
                //throw the error response json
                JsonResponse::error($e);
                return;
            }
            
            //if error not defined respond as internal error
            JsonResponse::internalError($e);
        }
    }
?>