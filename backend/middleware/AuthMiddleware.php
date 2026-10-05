<?php
    class AuthMiddleware{
        //require token
        public function __construct(private IAuthTokenRepository $authTokenRepository,private IUserRepository $userRepository,private Log $log,private RequestContext $requestContext){}

        
        public function requireToken(){
            
            //get the token
            $token = Request::getToken();
            //if no token throw not specified or incorrect format
            if(!$token){
                throw new UnauthorizedException("Bearer token not specified or in incorrect format");
            }

            //hash the token
            $tokenHash = Token::hashToken($token);
            
            //find the token
            $auth = $this->authTokenRepository->findByTokenHash($tokenHash);
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
                $this->authTokenRepository->revokeByTokenHash($tokenHash);
                $this->log->tryAuthLog($auth['user_id'],'LOGIN_EXPIRED',$auth['id']);
                throw new UnauthorizedException("Token expired");
            }
            
            //get the user details
            $user = $this->userRepository->getUserRole($auth['user_id']);
            //set the requestContext with role and user_id
            $this->requestContext->setUser($auth['user_id'],$user['role']);
        }
        
        public function requireUser(){
            //if not user throw exception
            if($this->requestContext->getRole()!== 'USER'){
                throw new ForbiddenException();
            }
        }

        public function requireAdmin(){
            //if not admin throw exception
            if($this->requestContext->getRole()!== 'ADMIN'){
                throw new ForbiddenException();
            }
        }
    }
?>