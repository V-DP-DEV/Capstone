<?php
    class AuthTokenRepository{
        //create token
        public static function create(int $userId,string $token,string $expiresAt,string $deviceId,string $deviceName){
            return Db::execute("INSERT INTO auth_tokens (user_id,token,expires_at,device_id,device_name) VALUES (?,?,?,?,?)",[$userId,$token,$expiresAt,$deviceId,$deviceName]);
        }
        //find token by hash
        public static function findByTokenHash(string $tokenHash){
            return Db::queryOne("SELECT * FROM auth_tokens WHERE token = ?",[$tokenHash]);
        }
        //revoke the token by hash
        public static function revokeByTokenHash(string $tokenHash){
            return Db::execute("UPDATE auth_tokens SET revoked_at = NOW() WHERE token = ? AND revoked_at IS NULL",[$tokenHash]);
        }
        
        //refresh token
        public static function refreshToken(int $id){
            return Db::execute("UPDATE auth_tokens SET expires_at = DATE_ADD(NOW(), INTERVAL 30 DAY) WHERE id = ?",[$id]);
        }
    }
?>