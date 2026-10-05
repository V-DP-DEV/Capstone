<?php
    interface IUserRepository{
        //find user by email
        public function findByEmail(string $email);

        //get user role
        public function getUserRole(int $userId);

        public function createUser(string $email, string $password, string $role,string $firstname,string $surname);
        public function getPassword(int $userId);
        public function delete(int $id);
        public function updatePassword(int $userId, string $newPasswordHash);
    }
?>