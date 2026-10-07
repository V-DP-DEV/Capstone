<?php
    class RefreshTokenRepository implements IRefreshTokenRepository{
        public function __construct(private Db $db){}

        public function create(int $userId,string $token,string $expiresAt,string $deviceId,string $deviceName){
            return $this->db->execute("INSERT INTO refresh_tokens (user_id,token_hash,expires_at,device_id,device_name) VALUES (?,?,?,?,?)",[$userId,$token,$expiresAt,$deviceId,$deviceName]);
        }

        public function findRefreshTokenByHash(string $tokenHash){
            return $this->db->queryOne("SELECT * FROM refresh_tokens WHERE token_hash = ?",[$tokenHash]);
        }

        public function revokeRefreshTokenByHash(string $tokenHash){
            return $this->db->execute("UPDATE refresh_tokens SET revoked_at = NOW() WHERE token_hash = ? AND revoked_at IS NULL",[$tokenHash]);
        }
        public function revokeAllByUserId(int $userId){
            return $this->db->execute("UPDATE refresh_tokens SET revoked_at = NOW() WHERE user_id = ? AND revoked_at IS NULL",[$userId]);
        }
    }
?>