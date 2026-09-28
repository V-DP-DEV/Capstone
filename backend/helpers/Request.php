<?php
    class Request{

        public static function json():array{
            $rawBody = file_get_contents('php://input');
            $data = json_decode($rawBody,true);

            if(!is_array($data)){
                throw new InvalidJsonException();
            }
            return self::cleanData($data);
        } 

        private static function cleanData(array $data):array{
            foreach($data as $key=>$value){
                if(is_array($value)){
                    $data[$key] = self::cleanData($value);
                }
                else{
                    $data[$key] = htmlspecialchars(trim((string)$value),ENT_QUOTES,'UTF-8');
                }
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

            if(empty($authorization)){
                return null;
            }
            
            //validates entire token, only valid characters a-Z and 0-9
            if(preg_match('/^Bearer\s([a-zA-Z0-9]+)$/',$authorization,$matches)){
                return $matches[1];
            }

            return null;
        }
    }
?>