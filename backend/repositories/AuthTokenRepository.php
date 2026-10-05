<?php
    class AuthTokenRepository implements IAuthTokenRepository{
        public function __construct(private Db $db){        }
        //create token
        public function create(int $userId,string $token,string $expiresAt,string $deviceId,string $deviceName){
            return $this->db->execute("INSERT INTO auth_tokens (user_id,token,expires_at,device_id,device_name) VALUES (?,?,?,?,?)",[$userId,$token,$expiresAt,$deviceId,$deviceName]);
        }
        //find token by hash
        public function findByTokenHash(string $tokenHash){
            return $this->db->queryOne("SELECT * FROM auth_tokens WHERE token = ?",[$tokenHash]);
        }
        //revoke the token by hash
        public function revokeByTokenHash(string $tokenHash){
            return $this->db->execute("UPDATE auth_tokens SET revoked_at = NOW() WHERE token = ? AND revoked_at IS NULL",[$tokenHash]);
        }
        
        //refresh token
        public function refreshToken(int $id){
            return $this->db->execute("UPDATE auth_tokens SET expires_at = DATE_ADD(NOW(), INTERVAL 30 DAY) WHERE id = ?",[$id]);
        }
        public function revokeAllByUserId(int $userId){
            return $this->db->execute("UPDATE auth_tokens SET revoked_at = NOW() WHERE user_id = ? AND revoked_at IS NULL",[$userId]);
        }
    }
?>