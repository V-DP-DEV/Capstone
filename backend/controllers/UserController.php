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
    }
?>