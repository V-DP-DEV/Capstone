<?php
    class LogRepository implements ILogRepository{
        //log method for auth
        //log ip
        public function __construct(private Db $db){}

        public function authLog(int $userId,string $action,$tokenId = null){
            return $this->db->execute("INSERT INTO auth_logs (user_id,action,token_id,ip_address) VALUES (?,?,?,?)",[$userId,$action,$tokenId,$_SERVER['REMOTE_ADDR']]);
        }

        public function auditLog(int $userId,string $action,string $entityType,int $entityId){
            return $this->db->execute("INSERT INTO audit_logs (user_id,action,entity_type,entity_id,ip_address) VALUES (?,?,?,?,?)",[$userId,$action,$entityType,$entityId,$_SERVER['REMOTE_ADDR']]);
        }
    }
?>