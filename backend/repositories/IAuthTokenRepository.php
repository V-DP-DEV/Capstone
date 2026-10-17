<?php
    interface IAuthTokenRepository{
        //create token
        public function create(int $userId,string $token,string $expiresAt,string $deviceId,string $deviceName):int;
        //find token by hash
        public function findByTokenHash(string $tokenHash):array|false;
        //revoke the token by hash
        public function revokeByTokenHash(string $tokenHash):int;
        
        //refresh token
        public function refreshToken(int $id):int;
        public function revokeAllByUserId(int $userId):int;
    }
?>