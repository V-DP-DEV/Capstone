<?php
    //used to have structure json response
    class JsonResponse{
        //if success
        public static function success($data =null){
            //return response code
            http_response_code(200);

            //return json and if data add
            echo json_encode([
                "success"=>true,
                "data"=>$data,
                "error"=>null
            ]);
        }

        public static function error(ApiException $e){
            //return response code from error
            http_response_code($e->statusCode);

            //return json, code, message and any details

            echo json_encode([
                "success"=>false,
                "data"=>null,
                "error"=>[
                    "code"=>$e->errorCode,
                    "message" => $e->getMessage(),
                    "details" =>$e->data
                ]
            ]);
        }

        public static function internalError($e){
            //return default response code
            http_response_code(500);

            //return json and extra debugging. Remove in production
            echo json_encode([
                "success"=>false,
                "data"=>null,
                "error"=>[
                    "code"=>"INTERNAL_ERROR",
                    "message" => "An unexpected error occurred",
                    "details" => [
                        "file"=>$e->getFile(),
                        "line"=>$e->getLine(),
                        "message"=>$e->getMessage()
                    ]
                ]
            ]);
        }
    }
?>