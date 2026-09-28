<?php
    class AuthMiddleware{
        public static function requireToken(){
            

            $token = Request::getToken();
            if(!$token){
                throw new UnauthorizedException("Bearer token not specified or in incorrect format");
            }

            $tokenHash = Token::hashToken($token);
            
            $auth = AuthTokenRepository::findByTokenHash($tokenHash);
            if(!$auth){
                throw new UnauthorizedException();
            }

            if($auth['revoked_at'] !== null){
                throw new UnauthorizedException("Token revoked");
            }

            if($auth['expires_at'] < date('Y-m-d H:i:s')){
                AuthTokenRepository::revokeByTokenHash($tokenHash);
                Log::tryAuthLog($auth['user_id'],'LOGIN_EXPIRED',$auth['id']);
                throw new UnauthorizedException("Token expired");
            }
            
            $user = UserRepository::getUserRole($auth['user_id']);
            RequestContext::setUser($auth['user_id'],$user['role']);
        }
        
        public static function requireUser(){
            if(RequestContext::getRole()!== 'USER'){
                throw new ForbiddenException();
            }
        }

        public static function requireAdmin(){
            if(RequestContext::getRole()!== 'ADMIN'){
                throw new ForbiddenException();
            }
        }
    }
?>