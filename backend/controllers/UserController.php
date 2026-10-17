<?php
    class UserController{
        public function __construct(private UserService $userService, private RequestContext $requestContext){}


        public function delete(){
            //requires methods delete
            Request::requireMethod("DELETE");
            //gets the data 
            $data = Request::json();
            //passes the data
            $result = $this->userService->deleteUser($data);
            //returns result, and other errors will be thrown to error exception handler
            JsonResponse::success($result);
        }

        public function changePassword(){
            //requires method patch
            Request::requireMethod("PATCH");
            //gets the data
            $data = Request::json();
            //passes the data on
            $result = $this->userService->changePassword($data);
            //returns result, and other errors will be thrown to error exception handler
            JsonResponse::success($result);
        }
    }
?>