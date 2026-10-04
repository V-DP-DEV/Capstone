<?php
    class UserRepository implements IUserRepository{
        //find user by email
        public function __construct(private Db $db){}
        public function findByEmail(string $email){
            return $this->db->queryOne("SELECT * FROM users WHERE email = ?",[$email]);
        }

        //get user role
        public function getUserRole(int $userId){
            return $this->db->queryOne("SELECT role FROM users WHERE id = ?",[$userId]);
        }

        public function createUser(string $email, string $password, string $role,string $firstname,string $surname){
            return $this->db->execute("INSERT INTO users (email,password,role,firstname,surname) VALUES (?,?,?,?,?)",[$email,$password,$role,$firstname,$surname]);
        }
        public function getPassword(int $userId){
            return $this->db->queryOne("SELECT password FROM users WHERE id = ?",[$userId]);
        }
        public function delete(int $id){
            return $this->db->execute("DELETE FROM users WHERE id = ?",[$id]);
        }
    }
?>