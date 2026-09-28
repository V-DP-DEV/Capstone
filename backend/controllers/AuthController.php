<?php
    class AuthController{
        public function login(){
            Request::requireMethod("POST");
            $data = Request::json();
            $result = AuthService::login($data);
            JsonResponse::success($result);
        }

        public function logout(){
            Request::requireMethod("POST");
            $token = Request::getToken();
            $result = AuthService::logout($token);
            JsonResponse::success();
        }

        public function refresh(){
            Request::requireMethod("POST");
            $token = Request::getToken();
            $result = AuthService::refresh($token);
            JsonResponse::success($result);
        }
    }
?>