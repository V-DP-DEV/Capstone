<?php
    class RequestContext{
       private static ?int $userId = null;
        private static ?string $role = null;

        public static function setUser(int $userId,string $role){
            self::$userId = $userId;
            self::$role = $role;
        }

        public static function getUserId():int{
            return self::$userId;
        }

        public static function getRole():string{
            return self::$role;
        } 
    }
?>