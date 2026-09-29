<?php
    class UserRepository{
        //find user by email
        public static function findByEmail(string $email){
            return Db::queryOne("SELECT * FROM users WHERE email = ?",[$email]);
        }

        //get user role
        public static function getUserRole(int $userId){
            return Db::queryOne("SELECT role FROM users WHERE id = ?",[$userId]);
        }
    }
?>