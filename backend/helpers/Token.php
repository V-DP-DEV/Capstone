<?php
    class Token{
        public static function generateToken($length = 32){
            return bin2hex(random_bytes($length));
        }
        public static function hashToken($token){
            return hash('sha256', $token);
        }
    }
?>