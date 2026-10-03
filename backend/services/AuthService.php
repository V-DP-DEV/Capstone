<?php
    class AuthService{
        public static function login($data){
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
            $user = UserRepository::findByEmail($data['email']);

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
            Db::transaction(function() use ($user,$tokenHash,$expiresAt,$refreshTokenHash,$refreshExpiresAt,$data){
                AuthTokenRepository::create($user['id'],$tokenHash,$expiresAt,$data['deviceId'],$data['deviceName']);
                RefreshTokenRepository::create($user['id'],$refreshTokenHash,$refreshExpiresAt,$data['deviceId'],$data['deviceName']);
            });

            //try to log the token
            Log::tryAuthLog($user['id'],'LOGIN',Db::lastInsertId());
            //return token, expiration and user role
            return ['token'=>$token,'tokenExpiresAt'=>$expiresAt,'refreshToken'=>$refreshToken,'refreshTokenExpiresAt'=>$refreshExpiresAt,'userRole'=>$user['role'] ];
        }

        public static function logout($token){
            //if no token return
            if(!$token){
                return;
            }
            //hash token
            $tokenHash = Token::hashToken($token);
            //find it in the db
            $dbToken = AuthTokenRepository::findByTokenHash($tokenHash);

            //if no token found return
            if(!$dbToken){
                return;
            }

            //revoke the token
            AuthTokenRepository::revokeByTokenHash($tokenHash);
            //try to log the auth
            Log::tryAuthLog($dbToken['user_id'],'LOGOUT',$dbToken['id']);
        }

        public static function refresh($refreshToken){
            //hash the token
            $refreshTokenHash = Token::hashToken($refreshToken);
            //find token in db
            $dbToken = RefreshTokenRepository::findRefreshTokenByHash($refreshTokenHash);
            $user = UserRepository::getUserRole($dbToken['user_id']);
            
            if(!$dbToken){
                throw new ValidationException("Token not found in db");
            }
            //check if token is revoked
            if($dbToken['revoked_at'] !== null){
                throw new ValidationException("Refresh token is revoked");
            }
            //check if token is expired
            if(strtotime($dbToken['expires_at']) < time()){
                Log::tryAuthLog($dbToken['user_id'],'EXPIRED',$dbToken['id']);
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
                
            Db::transaction(function() use ($refreshTokenHash,$dbToken,$tokenHash,$expiresAt,$newRefreshTokenHash,$newRefreshExpiresAt){
                //revoke the old refresh token
                RefreshTokenRepository::revokeRefreshTokenByHash($refreshTokenHash);
                AuthTokenRepository::create($dbToken['user_id'],$tokenHash,$expiresAt,$dbToken['device_id'],$dbToken['device_name']);
                //create new refresh token in db
                RefreshTokenRepository::create($dbToken['user_id'],$newRefreshTokenHash,$newRefreshExpiresAt,$dbToken['device_id'],$dbToken['device_name']);
            });
            

            //refresh the token
            //AuthTokenRepository::refreshToken($dbToken['id']);
            //try to log the action
            Log::tryAuthLog($dbToken['user_id'],'REFRESH',$dbToken['id']);

            //return the token, expiration
            return ['token'=>$token,'tokenExpiresAt'=>$expiresAt,'refreshToken'=>$newRefreshToken,'refreshTokenExpiresAt'=>$newRefreshExpiresAt,'userRole'=>$user['role'] ];
        }
    }
?>