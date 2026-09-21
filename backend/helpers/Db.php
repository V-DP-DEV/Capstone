<?php
    class Db{
        static private ?PDO $conn = null;

        private static function connect(){
            if (self::$conn ===null){
                self::$conn = new PDO("mysql:host=".getenv("DB_HOST") . ";dbname=". getenv('DB_NAME').";",getenv('DB_USER'),getenv('DB_PASSWORD'));
            }
            return self::$conn;
        }

        public static function queryOne($sql,$params = []){
            $stmt = self::connect()->prepare($sql);
            $stmt->execute($params);
            return $stmt->fetch(PDO::FETCH_ASSOC);
        }

        public static function queryAll($sql,$params = []){
            $stmt = self::connect()->prepare($sql);
            $stmt->execute($params);
            return $stmt->fetchAll(PDO::FETCH_ASSOC);
        }

        public static function execute($sql,$params = []){
            $stmt = self::connect()->prepare($sql);
            $stmt->execute($params);
            return $stmt->rowCount();
        }
    }
?>