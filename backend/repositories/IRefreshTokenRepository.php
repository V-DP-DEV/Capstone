<?php
    interface IRefreshTokenRepository{
        public function create(int $userId,string $token,string $expiresAt,string $deviceId,string $deviceName):int;
        public function findRefreshTokenByHash(string $tokenHash):array|false;
        public function revokeRefreshTokenByHash(string $tokenHash):int;
        public function revokeAllByUserId(int $userId):int;
    }
?>