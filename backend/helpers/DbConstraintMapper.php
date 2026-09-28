<?php
    class DbConstraintMapper{
        public static function map(Throwable $e){
            if(!self::isConstraintViolationException($e)){
                return $e;
            }
            $data = self::getConstraintViolation($e);
            if(!$data){
                return $e;
            }
            return new FieldValidationException("Validation errors occurred", $data);
        }

        private static function isConstraintViolationException(Throwable $e): bool{
            $code = $e->errorInfo[1] ?? null;
            return $e instanceof PDOException && in_array($code,[1062,1451,1452]);
        }

        private static function getConstraintViolation(Throwable $e): array{
            foreach (self::MAP as $constraint => $info) {
    				if (strpos($e->errorInfo[2], $constraint) !== false) {
                        $data = [];
                        $data[$info['field']] = $info['message'];
                        return $data;
    				}
				}
            return [];
        }

        public const MAP= [

        ];
    }
?>