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
            $expiresAt = date('Y-m-d H:i:s', strtotime('+30 days'));
            //create the token in db
            AuthTokenRepository::create($user['id'],$tokenHash,$expiresAt,$data['deviceId'],$data['deviceName']);

            //try to log the token
            Log::tryAuthLog($user['id'],'LOGIN',Db::lastInsertId());
            //return token, expiration and user role
            return ['token'=>$token,'expires_at'=>$expiresAt,'user_role'=>$user['role'] ];
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

        public static function refresh($token){
            //hash the token
            $tokenHash = Token::hashToken($token);
            //find token in db
            $dbToken = AuthTokenRepository::findByTokenHash($tokenHash);
            if(!$dbToken){
                throw new ValidationException("Token not found in db");
            }

            //refresh the token
            AuthTokenRepository::refreshToken($dbToken['id']);
            //try to log the action
            Log::tryAuthLog($dbToken['user_id'],'REFRESH',$dbToken['id']);

            //return the token, expiration
            return ['token'=>$token,'expires_at'=>date('Y-m-d H:i:s', strtotime('+30 days'))];
        }
    }
?>