<?php
    class JsonResponse{
        public static function success($data =null){
            http_response_code(200);

            echo json_encode([
                "success"=>true,
                "data"=>$data,
                "error"=>null
            ]);
        }

        public static function error(ApiException $e){
            http_response_code($e->statusCode);

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
            http_response_code(500);

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