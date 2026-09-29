<?php
    class Db{
        //static conn used and is deleted when request ends
        static private ?PDO $conn = null;

        private static function connect(){
            //if no connection exists
            if (self::$conn ===null){
                //create connection with env values
                self::$conn = new PDO("mysql:host=".getenv("DB_HOST") . ";dbname=". getenv('DB_NAME').";",getenv('DB_USER'),getenv('DB_PASSWORD'));

            }
            //set time execution to UTC for standards
            self::$conn->exec("SET time_zone ='+00:00'");
            //return the conn
            return self::$conn;
        }

        public static function queryOne($sql,$params = []){
            //connect to db, and prepare sql
            $stmt = self::connect()->prepare($sql);
            //add params to prevent sql injection
            $stmt->execute($params);
            //fetch only one record
            return $stmt->fetch(PDO::FETCH_ASSOC);
        }

        public static function queryAll($sql,$params = []){
            //connect to db, and prepare sql
            $stmt = self::connect()->prepare($sql);
            //add params to prevent sql injection
            $stmt->execute($params);
            //fetch all records
            return $stmt->fetchAll(PDO::FETCH_ASSOC);
        }

        public static function execute($sql,$params = []){
            //connect to db, and prepare sql
            $stmt = self::connect()->prepare($sql);
            //add params to prevent sql injection
            $stmt->execute($params);
            //fetch all records changed
            return $stmt->rowCount();
        }

        public static function lastInsertId(){
            //fetch last insertedId
            return self::$conn->lastInsertId();
        }

        public static function transaction(callable $callback){
            
            try{
                //begin a transaction
                self::connect()->beginTransaction();
                //call the function attached
                $result = $callback();
                //if no errors commit
                self::$conn->commit();
                //return the result of the callback
                return $result;
            }
            //if errors
            catch(Exception $e){
                //rollback
                self::$conn->rollBack();
                //re throw to global handler to handle
                throw $e;
            }
        }
    }
?>