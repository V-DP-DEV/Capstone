<?php
    //gives methods to generate and has the token
    class Token{
        //create random generate token, with 32 bytes
        public static function generateToken($length = 32){
            return bin2hex(random_bytes($length));
        }
        //hash the token using sha256
        public static function hashToken($token){
            return hash('sha256', $token);
        }
    }
?>