<?php
    class AuthTokenRepository{
        public static function create(int $userId,string $token,string $expiresAt,string $deviceId,string $deviceName){
            return Db::execute("INSERT INTO auth_tokens (user_id,token,expires_at,device_id,device_name) VALUES (?,?,?,?,?)",[$userId,$token,$expiresAt,$deviceId,$deviceName]);
        }
        public static function findByTokenHash(string $tokenHash){
            return Db::queryOne("SELECT * FROM auth_tokens WHERE token = ?",[$tokenHash]);
        }
        public static function revokeByTokenHash(string $tokenHash){
            return Db::execute("UPDATE auth_tokens SET revoked_at = NOW() WHERE token = ? AND revoked_at IS NULL",[$tokenHash]);
        }
        public static function updateExpiration(int $id){
            return Db::execute("UPDATE auth_tokens SET expires_at = DATE_ADD(NOW(), INTERVAL 30 DAY) WHERE id = ?",[$id]);
        }
    }
?>