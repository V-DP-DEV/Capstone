<?php
    class AuthService{


        public function __construct(
            private IUserRepository $userRepository,
            private IAuthTokenRepository $authTokenRepository,
            private IRefreshTokenRepository $refreshTokenRepository,
            private Log $log,
            private Db $db
        ){}

        public function login($data){
            //define field errors
            $fieldErrors = [];
            //if email empty add error
            if(empty($data['email'])){
                $fieldErrors['email'] = "Email is required";
            }
            //if password empty add error
            if(empty($data['password'])){
                $fieldErrors['password'] = "Password is required";
            }
            //if errors throw field validation error
            if(!empty($fieldErrors)){
                throw new FieldValidationException("Validation failed", $fieldErrors);
            }

            //find user by email
            $user = $this->userRepository->findByEmail($data['email']);

            //if user empty and password not match, throw error
            if(!$user || !password_verify($data['password'],$user['password'])){
                throw new UnauthorizedException("Invalid credentials");
            }
            //generate the token
            $token = Token::generateToken();
            //has token
            $tokenHash = Token::hashToken($token);
            //set expiration
            $expiresAt = date('Y-m-d H:i:s', strtotime('+15 minutes'));
            $refreshToken = Token::generateToken();
            $refreshTokenHash = Token::hashToken($refreshToken);
            $refreshExpiresAt = date('Y-m-d H:i:s', strtotime('+30 days'));
            //create the token in db
            //AuthTokenRepository::create($user['id'],$tokenHash,$expiresAt,$data['deviceId'],$data['deviceName']);
            $this->db->transaction(function() use ($user,$tokenHash,$expiresAt,$refreshTokenHash,$refreshExpiresAt,$data){
                $this->authTokenRepository->create($user['id'],$tokenHash,$expiresAt,$data['deviceId'],$data['deviceName']);
                $this->refreshTokenRepository->create($user['id'],$refreshTokenHash,$refreshExpiresAt,$data['deviceId'],$data['deviceName']);
            });

            //try to log the token
            $this->log->tryAuthLog($user['id'],'LOGIN',$this->db->lastInsertId());
            //return token, expiration and user role
            return ['token'=>$token,'tokenExpiresAt'=>TimeUtil::toMillis($expiresAt),'refreshToken'=>$refreshToken,'refreshTokenExpiresAt'=>TimeUtil::toMillis($refreshExpiresAt),'userRole'=>$user['role'] ];
        }

        public function logout($token){
            //if no token return
            if(!$token){
                return;
            }
            //hash token
            $tokenHash = Token::hashToken($token);
            //find it in the db
            $dbToken = $this->authTokenRepository->findByTokenHash($tokenHash);

            //if no token found return
            if(!$dbToken){
                return;
            }

            //revoke the token
            $this->authTokenRepository->revokeByTokenHash($tokenHash);
            //try to log the auth
            $this->log->tryAuthLog($dbToken['user_id'],'LOGOUT',$dbToken['id']);
        }

        public function logoutAllDevices($token){
            //if no token return
            if(!$token){
                return;
            }
            //hash token
            $tokenHash = Token::hashToken($token);
            //find it in the db
            $dbToken = $this->authTokenRepository->findByTokenHash($tokenHash);

            //if no token found return
            if(!$dbToken){
                return;
            }

            //revoke all tokens for the user
            $this->db->transaction(function() use ($dbToken){
                $this->authTokenRepository->revokeAllByUserId($dbToken['user_id']);
                $this->refreshTokenRepository->revokeAllByUserId($dbToken['user_id']);
            });
            //try to log the auth
            $this->log->tryAuthLog($dbToken['user_id'],'LOGOUT_ALL',$dbToken['id']);
        }

        public function refresh($refreshToken){
            //hash the token
            $refreshTokenHash = Token::hashToken($refreshToken);
            //find token in db
            $dbToken = $this->refreshTokenRepository->findRefreshTokenByHash($refreshTokenHash);
            $user = $this->userRepository->getUserRole($dbToken['user_id']);
            
            if(!$dbToken){
                throw new ValidationException("Token not found in db");
            }
            //check if token is revoked
            if($dbToken['revoked_at'] !== null){
                throw new ValidationException("Refresh token is revoked");
            }
            //check if token is expired
            if(strtotime($dbToken['expires_at']) < time()){
                $this->log->tryAuthLog($dbToken['user_id'],'EXPIRED',$dbToken['id']);
                throw new ValidationException("Refresh token is expired");
            }
            
            
            //generate new refresh token
                //generate the token
                $token = Token::generateToken();
                //has token
                $tokenHash = Token::hashToken($token);
                //set expiration
                $expiresAt = date('Y-m-d H:i:s', strtotime('+15 minutes'));
                
                $newRefreshToken = Token::generateToken();
                $newRefreshTokenHash = Token::hashToken($newRefreshToken);
                $newRefreshExpiresAt = date('Y-m-d H:i:s', strtotime('+30 days'));
                
            $this->db->transaction(function() use ($refreshTokenHash,$dbToken,$tokenHash,$expiresAt,$newRefreshTokenHash,$newRefreshExpiresAt){
                //revoke the old refresh token
                $this->refreshTokenRepository->revokeRefreshTokenByHash($refreshTokenHash);
                $this->authTokenRepository->create($dbToken['user_id'],$tokenHash,$expiresAt,$dbToken['device_id'],$dbToken['device_name']);
                //create new refresh token in db
                $this->refreshTokenRepository->create($dbToken['user_id'],$newRefreshTokenHash,$newRefreshExpiresAt,$dbToken['device_id'],$dbToken['device_name']);
            });
            

            //refresh the token
            //AuthTokenRepository::refreshToken($dbToken['id']);
            //try to log the action
            $this->log->tryAuthLog($dbToken['user_id'],'REFRESH',$dbToken['id']);

            //return the token, expiration
            return ['token'=>$token,'tokenExpiresAt'=>TimeUtil::toMillis($expiresAt),'refreshToken'=>$newRefreshToken,'refreshTokenExpiresAt'=>TimeUtil::toMillis($newRefreshExpiresAt),'userRole'=>$user['role'] ];
        }

        public function signup($data){
            //define field errors
            $fieldErrors = [];
            //if email empty add error
            if(empty($data['email'])){
                $fieldErrors['email'] = "Email is required";
            }
            else if(!filter_var($data['email'], FILTER_VALIDATE_EMAIL)){
                $fieldErrors['email'] = "Email is not valid";
            }
            
            //if password empty add error
            if (strlen($data['password']) < 8) {
                $fieldErrors['password'] = "Password must be at least 8 characters";
            }
            else if (!preg_match('/\d/', $data['password'])) {
                $fieldErrors['password'] = "Password must contain a number";
            }
            else if (!preg_match('/[^a-zA-Z0-9]/', $data['password'])) {
                $fieldErrors['password'] = "Password must contain a special character";
            }

            //if firstname empty add error
            if(empty($data['firstname'])){
                $fieldErrors['firstname'] = "Firstname is required";
            }
            //if surname empty add error
            if(empty($data['surname'])){
                $fieldErrors['surname'] = "Surname is required";
            }
            //if errors throw field validation error
            if(!empty($fieldErrors)){
                throw new FieldValidationException("Validation failed", $fieldErrors);
            }

            //hash the password
            $hashedPassword = password_hash($data['password'], PASSWORD_DEFAULT);
            
            //create the user in db
            $this->userRepository->createUser($data['email'],$hashedPassword,'USER',$data['firstname'],$data['surname']);
        }
    }
?>