<?php
    interface IUserRepository{
        //find user by email
        public function findByEmail(string $email):array|false;

        //get user role
        public function getUserRole(int $userId):array|false;

        public function createUser(string $email, string $password, string $role,string $firstname,string $surname):int;
        public function getPassword(int $userId):array|false;
        public function delete(int $id):int;
        public function updatePassword(int $userId, string $newPasswordHash):int;
        public function updateDetails(string $firstname,string $surname,int $userId):int;
        public function getProfile(int $userId):array|false;
    }
?>