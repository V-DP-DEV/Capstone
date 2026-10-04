<?php
    class UserService{
        public function __construct(private UserRepository $userRepository, private RequestContext $requestContext){}

        public function deleteUser($data){
            
            $password = $this->userRepository->getPassword($this->requestContext->getUserId());
            if(!$password){
                throw new NotFoundException("User not found");
            }
            if(!password_verify($data['password'], $password['password'])){
                throw new UnauthorizedException("Invalid password");
            }
            
            $this->userRepository->delete($this->requestContext->getUserId());
        }
    }
?>