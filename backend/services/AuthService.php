<?php
    class AuthService{
        public static function login($data){
            $fieldErrors = [];
            if(empty($data['email'])){
                $fieldErrors['email'] = "Email is required";
            }
            if(empty($data['password'])){
                $fieldErrors['password'] = "Password is required";
            }
            if(!empty($fieldErrors)){
                throw new FieldValidationException("Validation failed", $fieldErrors);
            }

            $user = UserRepository::findByEmail($data['email']);

            if(!$user || !password_verify($data['password'],$user['password'])){
                throw new UnauthorizedException("Invalid credentials");
            }
            $token = Token::generateToken();
            $tokenHash = Token::hashToken($token);
            $expiresAt = date('Y-m-d H:i:s', strtotime('+1 hour'));

            AuthTokenRepository::create($user['id'],$tokenHash,$expiresAt,$data['deviceId'],$data['deviceName']);

            Log::tryAuthLog($user['id'],'LOGIN',Db::lastInsertId());
            return ['token'=>$token,'expires_at'=>$expiresAt,'user_role'=>$user['role'] ];
        }

        public static function logout($token){
            if(!$token){
                return;
            }
            $tokenHash = Token::hashToken($token);
            $dbToken = AuthTokenRepository::findByTokenHash($tokenHash);

            if(!$dbToken){
                return;
            }

            AuthTokenRepository::revokeByTokenHash($tokenHash);

            Log::tryAuthLog($dbToken['user_id'],'LOGOUT',$dbToken['id']);
        }

        public static function refresh($token){
            $tokenHash = Token::hashToken($token);
            $dbToken = AuthTokenRepository::findByTokenHash($tokenHash);

            AuthTokenRepository::updateExpiration($dbToken['id']);
            Log::tryAuthLog($dbToken['user_id'],'REFRESH',$dbToken['id']);

            return ['token'=>$token,'expires_at'=>date('Y-m-d H:i:s', strtotime('+30 days'))];
        }
    }
?>