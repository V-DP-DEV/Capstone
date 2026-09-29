<?php
    class AuthMiddleware{
        //require token
        public static function requireToken(){
            
            //get the token
            $token = Request::getToken();
            //if no token throw not specified or incorrect format
            if(!$token){
                throw new UnauthorizedException("Bearer token not specified or in incorrect format");
            }

            //hash the token
            $tokenHash = Token::hashToken($token);
            
            //find the token
            $auth = AuthTokenRepository::findByTokenHash($tokenHash);
            //if not found throw unauthorized
            if(!$auth){
                throw new UnauthorizedException();
            }

            //if revoked throw error revoked
            if($auth['revoked_at'] !== null){
                throw new UnauthorizedException("Token revoked");
            }

            //if expired, revoke token, log and throw expirer
            if($auth['expires_at'] < date('Y-m-d H:i:s')){
                AuthTokenRepository::revokeByTokenHash($tokenHash);
                Log::tryAuthLog($auth['user_id'],'LOGIN_EXPIRED',$auth['id']);
                throw new UnauthorizedException("Token expired");
            }
            
            //get the user details
            $user = UserRepository::getUserRole($auth['user_id']);
            //set the requestContext with role and user_id
            RequestContext::setUser($auth['user_id'],$user['role']);
        }
        
        public static function requireUser(){
            //if not user throw exception
            if(RequestContext::getRole()!== 'USER'){
                throw new ForbiddenException();
            }
        }

        public static function requireAdmin(){
            //if not admin throw exception
            if(RequestContext::getRole()!== 'ADMIN'){
                throw new ForbiddenException();
            }
        }
    }
?>