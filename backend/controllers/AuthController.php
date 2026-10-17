<?php
    class AuthController{
        public function __construct(private AuthService $authService){}
        
        public function login(){
            
            //require post method, throws error
            Request::requireMethod("POST");
            //get the json data
            $data = Request::json();
            //attempt the login, if any errors throws error
            $email = $data["email"] ?? null;
            $password = $data["password"] ?? null;
            $deviceName = $data["deviceName"] ?? null;
            $deviceId = $data["deviceId"] ?? null;
            $result = $this->authService->login($email,$password,$deviceId,$deviceName);
            //return response if no errors
            JsonResponse::success($result);
            
        }

        public function logout(){
            //require post method, throws error
            Request::requireMethod("POST");
            //get the token
            $token = Request::getToken()??null;
            //attempt the logout
            $result = $this->authService->logout($token);
            //return a response
            JsonResponse::success();
        }

        public function logoutAllDevices(){
            Request::requireMethod("POST");
            $token = Request::getToken()??null;
            $result = $this->authService->logoutAllDevices($token);
            JsonResponse::success();
        }

        public function refresh(){
            //require post method, throws error
            Request::requireMethod("POST");
            $data = Request::json();
            $refreshToken = $data['refreshToken']??null;
            //attempt refresh, if any errors throws error
            $result = $this->authService->refresh($refreshToken);
            //return a response if no errors
            JsonResponse::success($result);
        }

        public function signup(){        
            //requires method post    
            Request::requireMethod("POST");
            //gets the data
            $data = Request::json();
            //gives it to service to use
            $email = $data["email"] ?? null;
            $password = $data["password"] ?? null;
            $firstname = $data["firstname"] ?? null;
            $surname = $data["surname"] ?? null;

            $result = $this->authService->signup($email,$password,$firstname,$surname);
            //return a response if no errors
            JsonResponse::success($result);
        }
    }
?>