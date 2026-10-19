<?php
    class UserRepository implements IUserRepository{
        //find user by email
        public function __construct(private Db $db){}
        public function findByEmail(string $email):array|false{
            return $this->db->queryOne("SELECT * FROM users WHERE email = ?",[$email]);
        }

        //get user role
        public function getUserRole(int $userId):array|false{
            return $this->db->queryOne("SELECT role FROM users WHERE id = ?",[$userId]);
        }

        public function createUser(string $email, string $password, string $role,string $firstname,string $surname):int{
            return $this->db->execute("INSERT INTO users (email,password,role,firstname,surname) VALUES (?,?,?,?,?)",[$email,$password,$role,$firstname,$surname]);
        }
        public function getPassword(int $userId):array|false{
            return $this->db->queryOne("SELECT password FROM users WHERE id = ?",[$userId]);
        }
        public function delete(int $id):int{
            return $this->db->execute("DELETE FROM users WHERE id = ?",[$id]);
        }
        public function updatePassword(int $userId, string $newPasswordHash):int{
            return $this->db->execute("UPDATE users SET password = ? WHERE id = ?",[$newPasswordHash,$userId]);
        }

        public function updateDetails(string $firstname,string $surname,int $userId):int{
            $query = "UPDATE users SET  firstname = ?,surname = ? WHERE id =?";
            return $this->db->execute($query,[$firstname,$surname,$userId]);
        }
        
        public function getProfile(int $userId):array|false{
            return $this->db->queryOne("SELECT email,firstname,surname,role,created_at as createdAt FROM users WHERE id = ?",[$userId]);
        }
    }
?>