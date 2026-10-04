<?php
    class UserController{
        public function __construct(private UserService $userService, private RequestContext $requestContext){}


        public function delete(){
            
            Request::requireMethod("DELETE");
            $data = Request::json();
            $result = $this->userService->deleteUser($data);
            JsonResponse::success($result);
        }
    }
?>