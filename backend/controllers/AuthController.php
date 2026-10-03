<?php
    class AuthController{
        public function login(){
            //require post method, throws error
            Request::requireMethod("POST");
            //get the json data
            $data = Request::json();
            //attempt the login, if any errors throws error
            $result = AuthService::login($data);
            //return response if no errors
            JsonResponse::success($result);
        }

        public function logout(){
            //require post method, throws error
            Request::requireMethod("POST");
            //get the token
            $token = Request::getToken();
            //attempt the logout
            $result = AuthService::logout($token);
            //return a response
            JsonResponse::success();
        }

        public function refresh(){
            //require post method, throws error
            Request::requireMethod("POST");
            //get the token
            $token = Request::getToken();
            $data = Request::json();
            //attempt refresh, if any errors throws error
            $result = AuthService::refresh($data['refreshToken']);
            //return a response if no errors
            JsonResponse::success($result);
        }
    }
?>