<?php
    class UserService{
        public function __construct(private UserRepository $userRepository, private RequestContext $requestContext,private Log $log){}

        public function deleteUser(?string $password){
            //checks password in db
            $userPassword = $this->userRepository->getPassword($this->requestContext->getUserId());
            //if not found throw error
            if(!$userPassword){
                throw new NotFoundException("User not found");
            }
            //if hash doesnt match throw error
            if(!password_verify($password, $userPassword['password'])){
                throw new UnauthorizedException("Invalid password");
            }
            //try to delete the account
            $this->userRepository->delete($this->requestContext->getUserId());
            $this->log->tryAuditLog($this->requestContext->getUserId(),'DELETE_ACCOUNT_ATTEMPT','user',$this->requestContext->getUserId());
        }

        public function changePassword(?string $newPassword, ?string $oldPassword){
            //if password empty add error
            if (strlen($newPassword) < 8) {
                $fieldErrors['newPassword'] = "Password must be at least 8 characters";
            }
            //if password doenst have number
            else if (!preg_match('/\d/', $newPassword)) {
                $fieldErrors['newPassword'] = "Password must contain a number";
            }
            //if password doesnt have a special character
            else if (!preg_match('/[^a-zA-Z0-9]/', $newPassword)) {
                $fieldErrors['newPassword'] = "Password must contain a special character";
            }
            //if errors throw field validation error
            if(!empty($fieldErrors)){
                throw new FieldValidationException("Validation failed", $fieldErrors);
            }

            //get password
            $password = $this->userRepository->getPassword($this->requestContext->getUserId());
            //if not found throw error
            if(!$password){
                throw new NotFoundException("User not found");
            }
            //if doesnt match throw error
            if(!password_verify($oldPassword, $password['password'])){
                throw new UnauthorizedException("Invalid password");
            }
            //create new password hash and save
            $newPasswordHash = password_hash($newPassword, PASSWORD_DEFAULT);
            $this->userRepository->updatePassword($this->requestContext->getUserId(), $newPasswordHash);
            $this->log->tryAuditLog($this->requestContext->getUserId(),'CHANGE_PASSWORD','user',$this->requestContext->getUserId());
        }
    }
?>