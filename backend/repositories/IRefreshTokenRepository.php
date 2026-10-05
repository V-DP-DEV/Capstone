<?php
    interface IRefreshTokenRepository{
        public function create(int $userId,string $token,string $expiresAt,string $deviceId,string $deviceName);
        public function findRefreshTokenByHash(string $tokenHash);
        public function revokeRefreshTokenByHash(string $tokenHash);
        public function revokeAllByUserId(int $userId);
    }
?>