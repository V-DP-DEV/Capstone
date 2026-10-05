<?php
    class UserService{
        public function __construct(private UserRepository $userRepository, private RequestContext $requestContext,private Log $log){}

        public function deleteUser($data){
            
            $password = $this->userRepository->getPassword($this->requestContext->getUserId());
            if(!$password){
                throw new NotFoundException("User not found");
            }
            if(!password_verify($data['password'], $password['password'])){
                throw new UnauthorizedException("Invalid password");
            }
            $this->log->tryAuditLog($this->requestContext->getUserId(),'DELETE_ACCOUNT_ATTEMPT','user',$this->requestContext->getUserId());
            $this->userRepository->delete($this->requestContext->getUserId());
        }

        public function changePassword($data){
            //if password empty add error
            if (strlen($data['newPassword']) < 8) {
                $fieldErrors['newPassword'] = "Password must be at least 8 characters";
            }
            else if (!preg_match('/\d/', $data['newPassword'])) {
                $fieldErrors['newPassword'] = "Password must contain a number";
            }
            else if (!preg_match('/[^a-zA-Z0-9]/', $data['newPassword'])) {
                $fieldErrors['newPassword'] = "Password must contain a special character";
            }
            //if errors throw field validation error
            if(!empty($fieldErrors)){
                throw new FieldValidationException("Validation failed", $fieldErrors);
            }

            $password = $this->userRepository->getPassword($this->requestContext->getUserId());
            if(!$password){
                throw new NotFoundException("User not found");
            }
            if(!password_verify($data['oldPassword'], $password['password'])){
                throw new UnauthorizedException("Invalid password");
            }
            $newPasswordHash = password_hash($data['newPassword'], PASSWORD_DEFAULT);
            $this->userRepository->updatePassword($this->requestContext->getUserId(), $newPasswordHash);
            $this->log->tryAuditLog($this->requestContext->getUserId(),'CHANGE_PASSWORD','user',$this->requestContext->getUserId());
        }
    }
?>