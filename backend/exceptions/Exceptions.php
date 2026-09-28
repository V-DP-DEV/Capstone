<?php
    class ApiException extends Exception{
        public int $statusCode;
        public string $errorCode;
        public mixed $data;

        public function __construct($statusCode,$errorCode,$message,$data =null){
            parent::__construct($message);

            $this->statusCode = $statusCode;
            $this->errorCode = $errorCode;
            $this->data = $data;
        }
    }

    class UnauthorizedException extends ApiException{
        public function __construct(
            string $message = "You are not authenticated to perform this action"
            ){
            parent::__construct(
                401,
                "UNAUTHORIZED",
                $message
            );
        }
    }

    class ForbiddenException extends ApiException{
        public function __construct(
            string $message = "You do not have permission to perform this action"
            ){
            parent::__construct(
                403,
                "FORBIDDEN",
                $message
            );
        }
    }

    class NotFoundException extends ApiException{
        public function __construct(
            string $message="The requested resource could not be found"
        ){
            parent::__construct(
                404,
                "NOT_FOUND",
                $message
            );
        }
    }

    class ValidationException extends ApiException{
        public function __construct(
            string $message ="Validation errors occurred"
        ){
            parent::__construct(
                400,
                "VALIDATION_ERROR",
                $message
            );
        }
    }

    class FieldValidationException extends ApiException{
        public function __construct(
            string $message ="Validation errors occurred",?array $fields = null
        ){
            parent::__construct(
                400,
                "FIELD_VALIDATION_ERROR",
                $message,
                $fields
            );
        }
    }

    class InvalidJsonException extends ApiException{
        public function __construct(
            string $message = "Invalid json request"
        ){
            parent::__construct(
                400,
                "INVALID_JSON",
                $message
            );
        }
    }

    class MethodNotAllowedException extends ApiException{
        public function __construct(
            string $message = "Method not allowed for this endpoint"
        ){
            parent::__construct(
                405,
                "METHOD_NOT_ALLOWED",
                $message
            );
        }
    }
?>