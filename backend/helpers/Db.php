<?php
    class Db{

        //static conn used and is deleted when request ends
        private ?PDO $conn = null;

        private function connect(){
            //if no connection exists
            if ($this->conn ===null){
                //create connection with env values
                $this->conn = new PDO("mysql:host=".getenv("DB_HOST") . ";dbname=". getenv('DB_NAME').";",getenv('DB_USER'),getenv('DB_PASSWORD'));

            }
            //set time execution to UTC for standards
            $this->conn->exec("SET time_zone ='+00:00'");
            //return the conn
            return $this->conn;
        }

        public function queryOne($sql,$params = []){
            //connect to db, and prepare sql
            $stmt = $this->connect()->prepare($sql);
            //add params to prevent sql injection
            $stmt->execute($params);
            //fetch only one record
            return $stmt->fetch(PDO::FETCH_ASSOC);
        }

        public function queryAll($sql,$params = []){
            //connect to db, and prepare sql
            $stmt = $this->connect()->prepare($sql);
            //add params to prevent sql injection
            $stmt->execute($params);
            //fetch all records
            return $stmt->fetchAll(PDO::FETCH_ASSOC);
        }

        public function execute($sql,$params = []){
            //connect to db, and prepare sql
            $stmt = $this->connect()->prepare($sql);
            //add params to prevent sql injection
            $stmt->execute($params);
            //fetch all records changed
            return $stmt->rowCount();
        }

        public function lastInsertId(){
            //fetch last insertedId
            return $this->conn->lastInsertId();
        }

        public function transaction(callable $callback){
            
            try{
                //begin a transaction
                $this->connect()->beginTransaction();
                //call the function attached
                $result = $callback();
                //if no errors commit
                $this->conn->commit();
                //return the result of the callback
                return $result;
            }
            //if errors
            catch(Exception $e){
                //rollback
                $this->conn->rollBack();
                //re throw to global handler to handle
                throw $e;
            }
        }
        public function beginTransaction(){
            $this->connect()->beginTransaction();
        }
        public function commit(){
            $this->connect()->commit();
        }
        public function rollBack(){
            $this->connect()->rollBack();
        }
    }
?>