<?php
    //used to store data to persist over request after middleware worked
    class RequestContext{
       private static ?int $userId = null;
        private static ?string $role = null;

        //set the user
        public static function setUser(int $userId,string $role){
            self::$userId = $userId;
            self::$role = $role;
        }

        //getters
        public static function getUserId():int{
            return self::$userId;
        }

        public static function getRole():string{
            return self::$role;
        } 
    }
?>