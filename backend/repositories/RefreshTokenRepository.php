<?php
    class RefreshTokenRepository{
        public static function create(int $userId,string $token,string $expiresAt,string $deviceId,string $deviceName){
            return Db::execute("INSERT INTO refresh_tokens (user_id,token_hash,expires_at,device_id,device_name) VALUES (?,?,?,?,?)",[$userId,$token,$expiresAt,$deviceId,$deviceName]);
        }

        public static function findRefreshTokenByHash(string $tokenHash){
            return Db::queryOne("SELECT * FROM refresh_tokens WHERE token_hash = ?",[$tokenHash]);
        }

        public static function revokeRefreshTokenByHash(string $tokenHash){
            return Db::execute("UPDATE refresh_tokens SET revoked_at = NOW() WHERE token_hash = ? AND revoked_at IS NULL",[$tokenHash]);
        }
        public static function revokeAllByUserId(int $userId){
            return Db::execute("UPDATE refresh_tokens SET revoked_at = NOW() WHERE user_id = ? AND revoked_at IS NULL",[$userId]);
        }
    }
?>