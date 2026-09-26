<?php
    class ErrorHandler{
        public static function handle(Throwable $e){
            if($e instanceof ApiException){
                JsonResponse::error($e);
                return;
            }
            
            JsonResponse::internalError($e);
        }
    }
?>