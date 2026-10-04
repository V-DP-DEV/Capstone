<?php
    class AuthController{
        public function __construct(private AuthService $authService){}
        
        public function login(){
            
            //require post method, throws error
            Request::requireMethod("POST");
            //get the json data
            $data = Request::json();
            //attempt the login, if any errors throws error
            $result = $this->authService->login($data);
            //return response if no errors
            JsonResponse::success($result);
            
        }

        public function logout(){
            //require post method, throws error
            Request::requireMethod("POST");
            //get the token
            $token = Request::getToken();
            //attempt the logout
            $result = $this->authService->logout($token);
            //return a response
            JsonResponse::success();
        }

        public function logoutAllDevices(){
            Request::requireMethod("POST");
            $token = Request::getToken();
            $result = $this->authService->logoutAllDevices($token);
            JsonResponse::success();
        }

        public function refresh(){
            //require post method, throws error
            Request::requireMethod("POST");
            //get the token
            $token = Request::getToken();
            $data = Request::json();
            //attempt refresh, if any errors throws error
            $result = $this->authService->refresh($data['refreshToken']);
            //return a response if no errors
            JsonResponse::success($result);
        }

        public function signup(){
            Request::requireMethod("POST");
            $data = Request::json();
            $result = $this->authService->signup($data);
            JsonResponse::success($result);
        }
    }
?>