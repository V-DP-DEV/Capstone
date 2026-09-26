<?php
    class AuthMiddleware{
        public static function handle(){
            $header = $_SERVER['HTTP_AUTHORIZATION'] ?? null;
            if(!$header || !str_starts_with($header,'Bearer ')){
                throw new UnauthorizedException();
            }

            $token = substr($header,7);
            $tokenHash = hash('sha256',$token);
            
            $auth = Db::queryOne("",[$tokenHash]);
            //do db check

            if(!$auth){
                throw new UnauthorizedException();
            }

            //extract from db call
            //RequestContext::setUser();
        }
        
        public static function requireUser(){
            if(RequestContext::getRole()!== 'user'){
                throw new ForbiddenException();
            }
        }

        public static function requireAdmin(){
            if(RequestContext::getRole()!== 'admin'){
                throw new ForbiddenException();
            }
        }
    }
?>