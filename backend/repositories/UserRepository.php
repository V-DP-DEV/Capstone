<?php
    class UserRepository{
        public static function findByEmail(string $email){
            return Db::queryOne("SELECT * FROM users WHERE email = ?",[$email]);
        }

        public static function getUserRole(int $userId){
            return Db::queryOne("SELECT role FROM users WHERE id = ?",[$userId]);
        }
    }
?>