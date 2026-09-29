<?php
    //used to get stuff of the request
    class Request{
        //get the body
        public static function json():array{
            //get the body and decode
            $rawBody = file_get_contents('php://input');
            $data = json_decode($rawBody,true);
            //if not array throw invalid json
            if(!is_array($data)){
                throw new InvalidJsonException();
            }
            //clean and sanitise the data and then return
            return self::cleanData($data);
        } 

        private static function cleanData(array $data):array{
            //for each data key pair value
            foreach($data as $key=>$value){
                //if array call recursively
                if(is_array($value)){
                    $data[$key] = self::cleanData($value);
                }
                //if just key, only UTF-8, trim and convert special characters
                else{
                    $data[$key] = htmlspecialchars(trim((string)$value),ENT_QUOTES,'UTF-8');
                }
            }
            //return the newly cleaned data
            return $data;
        }

        //require specific method
        public static function requireMethod(string $method){
            //if method doesnt match throw error
            if ($_SERVER['REQUEST_METHOD'] !== $method) {
                throw new MethodNotAllowedException();
            }
        }

        public static function getToken(){
            //get headers auth
            $authorization = getallheaders()['Authorization'] ?? '';

            //if no header return null
            if(empty($authorization)){
                return null;
            }
            
            //validates entire token, only valid characters a-Z and 0-9
            if(preg_match('/^Bearer\s([a-zA-Z0-9]+)$/',$authorization,$matches)){
                return $matches[1];
            }

            //if didnt match pregmatch return null
            return null;
        }
    }
?>