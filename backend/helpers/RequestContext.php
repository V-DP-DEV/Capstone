<?php
    //used to store data to persist over request after middleware worked
    class RequestContext{
       private ?int $userId = null;
        private ?string $role = null;

        //set the user
        public function setUser(int $userId,string $role){
            $this->userId = $userId;
            $this->role = $role;
        }

        //getters
        public function getUserId():int{
            return $this->userId;
        }

        public function getRole():string{
            return $this->role;
        } 
    }
?>