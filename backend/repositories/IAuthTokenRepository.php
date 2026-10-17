<?php
    interface IAuthTokenRepository{
        //create token
        public function create(int $userId,string $token,string $expiresAt,string $deviceId,string $deviceName);
        //find token by hash
        public function findByTokenHash(string $tokenHash);
        //revoke the token by hash
        public function revokeByTokenHash(string $tokenHash);
        
        //refresh token
        public function refreshToken(int $id);
        public function revokeAllByUserId(int $userId);
    }
?>