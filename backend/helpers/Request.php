<?php
    class Request{
        public static function json():array{
            $data = json_decode(
                file_get_contents("php://input"),
                true
            );

            if(!is_array($data)){
                throw new InvalidJsonException();
            }

            return $data;
        }

        public static function requireMethod(string $method){
            if ($_SERVER['REQUEST_METHOD'] !== $method) {
                throw new MethodNotAllowedException();
            }
        }

        public static function getToken(){
            $authorization = getallheaders()['Authorization'] ?? '';
            if(!str_starts_with($authorization,'Bearer ')){
                throw new UnauthorizedException("Bearer token not specified");
            }

            $token = substr($authorization,7);
            return $token;
        }
    }
?>