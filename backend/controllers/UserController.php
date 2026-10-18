<?php
    class UserController{
        public function __construct(private UserService $userService, private RequestContext $requestContext){}


        public function delete(){
            //requires methods delete
            Request::requireMethod("DELETE");
            //gets the data 
            $data = Request::json();
            $password = $data["password"]??null;
            //passes the data
            $result = $this->userService->deleteUser($password);
            //returns result, and other errors will be thrown to error exception handler
            JsonResponse::success($result);
        }

        public function changePassword(){
            //requires method patch
            Request::requireMethod("PATCH");
            //gets the data
            $data = Request::json();
            $oldPassword = $data["oldPassword"]??null;
            $newPassword = $data["newPassword"]??null;
            //passes the data on
            $result = $this->userService->changePassword($newPassword,$oldPassword);
            //returns result, and other errors will be thrown to error exception handler
            JsonResponse::success($result);
        }

        public function updateDetails(){
            Request::requireMethod("PATCH");
            $data = Request::json();
            $firstname = $data['firstname']??null;
            $surname = $data['surname']??null;
            $this->userService->updateDetails($firstname,$surname);
            JsonResponse::success();
        }

        public function getProfile(){
            Request::requireMethod("GET");
            $result = $this->userService->getProfile($this->requestContext->getUserId());
            JsonResponse::success($result);
        }
    }
?>