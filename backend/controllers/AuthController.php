<?php
    class AuthController{
        public function test(){
            $token = Request::getToken();
            JsonResponse::success(['token'=>getenv("DB_HOST")]);
        }

        public function login(){
            Request::requireMethod("POST");
            $data = Request::json();
            JsonResponse::success($data);
        }
    }
?>