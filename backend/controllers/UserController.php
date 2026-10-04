<?php
    class UserController{
        public function __construct(private UserService $userService, private RequestContext $requestContext){}


        public function delete(){
            
            Request::requireMethod("DELETE");
            $data = Request::json();
            $result = $this->userService->deleteUser($data);
            JsonResponse::success($result);
        }

        public function changePassword(){
            Request::requireMethod("PATCH");
            $data = Request::json();
            $result = $this->userService->changePassword($data);
            JsonResponse::success($result);
        }
    }
?>